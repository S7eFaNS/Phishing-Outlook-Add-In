package com.bank.phishaid.analysis.serviceLayer;

import com.bank.phishaid.analysis.entity.HeaderScoreLst;
import com.bank.phishaid.analysis.model.Indicator;
import com.bank.phishaid.initialization.entity.MailPathLst;
import com.bank.phishaid.initialization.entity.PhMail;
import com.bank.phishaid.analysis.repository.interfaces.IAnalysisHeaderReadRepo;
import com.bank.phishaid.analysis.repository.interfaces.IAnalysisHeaderRepo;
import com.bank.phishaid.analysis.serviceLayer.interfaces.IAnalysisHeaderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

@Service
class AnalysisHeaderService implements IAnalysisHeaderService {

    private static final Logger log = LoggerFactory.getLogger(AnalysisHeaderService.class);

    private final IAnalysisHeaderReadRepo readRepo;
    private final IAnalysisHeaderRepo headerRepo;

    private final ThreatScorer threatScorer = new ThreatScorer();

    AnalysisHeaderService(IAnalysisHeaderReadRepo readRepo, IAnalysisHeaderRepo headerRepo) {
        this.readRepo = readRepo;
        this.headerRepo = headerRepo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Set<Indicator> AnalysisHeader(UUID phMailId, UUID analysRsltId) {
        PhMail phMail = readRepo.findPhMail(phMailId);
        MailPathLst mailPath = readRepo.findMailPath(phMailId);

        Set<Indicator> fired = EnumSet.noneOf(Indicator.class);

        // SPF/DKIM/DMARC fire only on an explicit false; null (auth header absent) does not fire.
        if (Boolean.FALSE.equals(mailPath.getMailSpf())) fired.add(Indicator.SPF_FAIL);
        if (Boolean.FALSE.equals(mailPath.getMailDkim())) fired.add(Indicator.DKIM_FAIL);
        if (Boolean.FALSE.equals(mailPath.getMailDmarc())) fired.add(Indicator.DMARC_FAIL);

        // From-domain vs Reply-To / Return-Path; an absent (or unparseable) header counts as a match.
        String fromDomain = domainOf(phMail.getPhFrom());
        boolean replyToMatch = domainMatches(fromDomain, phMail.getRepTo());
        boolean retPathMatch = domainMatches(fromDomain, phMail.getRetPath());
        if (!replyToMatch) fired.add(Indicator.REPLY_TO_MISMATCH);
        if (!retPathMatch) fired.add(Indicator.RETURN_PATH_MISMATCH);

        int headScore = fired.isEmpty() ? 0 : threatScorer.score(fired).score();

        HeaderScoreLst row = new HeaderScoreLst();
        row.setSpfScore(token(mailPath.getMailSpf()));
        row.setDkimScore(token(mailPath.getMailDkim()));
        row.setDmarcScore(token(mailPath.getMailDmarc()));
        row.setReplyToMatch(replyToMatch);
        row.setRetPathMatch(retPathMatch);
        row.setHeadScore(headScore);
        headerRepo.Create(row, analysRsltId);

        log.info("Header analysis for phMail {}: {} indicator(s) fired, headScore {}",
                phMailId, fired.size(), headScore);
        return fired;
    }

    private static String token(Boolean authResult) {
        if (authResult == null) {
            return null;
        }
        return authResult ? "pass" : "fail";
    }

    private static String domainOf(String headerValue) {
        if (headerValue == null) {
            return null;
        }
        int at = headerValue.lastIndexOf('@');
        if (at < 0) {
            return null;
        }
        StringBuilder domain = new StringBuilder();
        for (int i = at + 1; i < headerValue.length(); i++) {
            char c = headerValue.charAt(i);
            if (c == '>' || c == ',' || c == ';' || Character.isWhitespace(c)) {
                break;
            }
            domain.append(c);
        }
        return domain.isEmpty() ? null : domain.toString().toLowerCase();
    }

    private static boolean domainMatches(String fromDomain, String headerValue) {
        if (headerValue == null || headerValue.isBlank()) {
            return true;
        }
        String otherDomain = domainOf(headerValue);
        if (otherDomain == null) {
            return true;
        }
        return otherDomain.equals(fromDomain);
    }
}
