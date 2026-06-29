package com.bank.phishaid.analysis.serviceLayer;

import com.bank.phishaid.analysis.dto.Verdict;
import com.bank.phishaid.analysis.entity.LinkScoreLst;
import com.bank.phishaid.analysis.model.Indicator;
import com.bank.phishaid.analysis.model.ScanResult;
import com.bank.phishaid.analysis.repository.interfaces.IAnalysisUrlReadRepo;
import com.bank.phishaid.analysis.repository.interfaces.IUrlScoreRepo;
import com.bank.phishaid.analysis.serviceLayer.interfaces.IAnalysisUrlService;
import com.bank.phishaid.analysis.serviceLayer.interfaces.IUrlAndAttachmentChecker;
import com.bank.phishaid.initialization.entity.LinkLst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
class AnalysisUrlService implements IAnalysisUrlService {

    private static final Logger log = LoggerFactory.getLogger(AnalysisUrlService.class);

    private final IAnalysisUrlReadRepo readRepo;
    private final IUrlScoreRepo urlScoreRepo;
    private final IUrlAndAttachmentChecker checker;

    private final ThreatScorer threatScorer = new ThreatScorer();

    AnalysisUrlService (IAnalysisUrlReadRepo readRepo, 
                        IUrlScoreRepo urlScoreRepo,
                        IUrlAndAttachmentChecker checker) {
        this.readRepo = readRepo;
        this.urlScoreRepo = urlScoreRepo;
        this.checker = checker;
    }

    @Override
    public ScanResult AnalyseUrls(UUID phMailId, UUID analysRsltId) {
        List<LinkLst> urls = readRepo.findUrls(phMailId);

        Set<Indicator> fired = EnumSet.noneOf(Indicator.class);
        List<LinkScoreLst> rows = new ArrayList<>(urls.size());
        int maliciousCount = 0;
        int skipped = 0;
        for (LinkLst url : urls) {
            Verdict verdict = checker.UrlCheck(url.getLinkUrl());
            if (!verdict.available()) {
                skipped++;
            }
            boolean malicious = verdict.available() && verdict.malicious();
            if (malicious) {
                maliciousCount++;
                fired.add(Indicator.MALICIOUS_URL);
            }
            int linkScore = malicious
                    ? threatScorer.score(EnumSet.of(Indicator.MALICIOUS_URL)).score()
                    : 0;
            LinkScoreLst row = new LinkScoreLst();
            row.setLinkScore(linkScore);
            row.setLinkLst(url); 
            rows.add(row);
        }

        // Persist one row + junction per URL occurrence.
        for (LinkScoreLst row : rows) {
            LinkScoreLst saved = urlScoreRepo.Create(row);
            urlScoreRepo.CreateJunctionColumn(analysRsltId, saved);
        }

        log.info("URL analysis for phMail {}: {} url(s) examined, {} malicious, {} skipped (VT unavailable)",
                phMailId, urls.size(), maliciousCount, skipped);
        return new ScanResult(fired, skipped == 0);
    }
}
