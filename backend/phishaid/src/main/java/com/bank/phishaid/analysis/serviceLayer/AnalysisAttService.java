package com.bank.phishaid.analysis.serviceLayer;

import com.bank.phishaid.analysis.dto.Verdict;
import com.bank.phishaid.analysis.entity.AttScoreLst;
import com.bank.phishaid.analysis.model.Indicator;
import com.bank.phishaid.analysis.model.ScanResult;
import com.bank.phishaid.analysis.repository.interfaces.IAnalysisAttReadRepo;
import com.bank.phishaid.analysis.repository.interfaces.IAttScoreRepo;
import com.bank.phishaid.analysis.serviceLayer.interfaces.IAnalysisAttService;
import com.bank.phishaid.analysis.serviceLayer.interfaces.IUrlAndAttachmentChecker;
import com.bank.phishaid.initialization.entity.AttachmentLst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
class AnalysisAttService implements IAnalysisAttService {

    private static final Logger log = LoggerFactory.getLogger(AnalysisAttService.class);

    private final IAnalysisAttReadRepo readRepo;
    private final IAttScoreRepo attScoreRepo;
    private final IUrlAndAttachmentChecker checker;


    private final ThreatScorer threatScorer = new ThreatScorer();

    AnalysisAttService(IAnalysisAttReadRepo readRepo, IAttScoreRepo attScoreRepo,
                       IUrlAndAttachmentChecker checker) {
        this.readRepo = readRepo;
        this.attScoreRepo = attScoreRepo;
        this.checker = checker;
    }

    @Override
    public ScanResult AnalyseAtt(UUID phMailId, UUID analysRsltId) {
        List<AttachmentLst> attachments = readRepo.findAttNames(phMailId);

        Set<Indicator> fired = EnumSet.noneOf(Indicator.class);
        List<AttScoreLst> rows = new ArrayList<>(attachments.size());
        int maliciousCount = 0;
        int skipped = 0;
        for (AttachmentLst attachment : attachments) {
            Verdict verdict = checker.AttCheck(attachment.getAttName());
            if (!verdict.available()) {
                skipped++;
            }
            boolean malicious = verdict.available() && verdict.malicious();
            if (malicious) {
                maliciousCount++;
                fired.add(Indicator.MALICIOUS_ATTACHMENT);
            }
            int attScore = malicious
                    ? threatScorer.score(EnumSet.of(Indicator.MALICIOUS_ATTACHMENT)).score()
                    : 0;
            AttScoreLst row = new AttScoreLst();
            row.setAttScore(attScore);
            row.setAttLst(attachment); 
            rows.add(row);
        }

        // Persist one row + junction per attachment occurrence.
        for (AttScoreLst row : rows) {
            AttScoreLst saved = attScoreRepo.Create(row);
            attScoreRepo.CreateJunctionColumn(analysRsltId, saved);
        }

        log.info("Attachment analysis for phMail {}: {} attachment(s) examined, {} malicious, {} skipped (VT unavailable)",
                phMailId, attachments.size(), maliciousCount, skipped);
        return new ScanResult(fired, skipped == 0);
    }
}
