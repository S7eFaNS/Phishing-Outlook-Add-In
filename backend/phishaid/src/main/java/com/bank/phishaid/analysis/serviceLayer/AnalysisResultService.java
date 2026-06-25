package com.bank.phishaid.analysis.serviceLayer;

import com.bank.phishaid.analysis.entity.AnalysRslt;
import com.bank.phishaid.analysis.model.Indicator;
import com.bank.phishaid.analysis.model.ScoreResult;
import com.bank.phishaid.analysis.repository.interfaces.IAnalysisResultRepo;
import com.bank.phishaid.analysis.serviceLayer.interfaces.IAnalysisResultService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
class AnalysisResultService implements IAnalysisResultService {

    private static final Logger log = LoggerFactory.getLogger(AnalysisResultService.class);

    private final IAnalysisResultRepo repo;

    private final ThreatScorer threatScorer = new ThreatScorer();

    AnalysisResultService(IAnalysisResultRepo repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UUID Create(UUID phMailId) {
        AnalysRslt result = new AnalysRslt();
        result.setTotalScore(0);
        result.setAnalysDesc(null);
        result.setFrwdToHost(false);
        UUID analysRsltId = repo.Create(result, phMailId).getAnalysRsltId();
        log.info("Analysis result row created {} for phMail {}", analysRsltId, phMailId);
        return analysRsltId;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void Update(UUID analysRsltId, Set<Indicator> fired, boolean incomplete) {
        ScoreResult scored = threatScorer.score(fired);

        AnalysRslt result = repo.Find(analysRsltId);
        result.setTotalScore(scored.score());
        result.setAnalysDesc(incomplete ? scored.level().name() + " (incomplete)" : scored.level().name());
        result.setFrwdToHost(headerAuthFailed(fired));
        repo.Update(result);

        log.info("Analysis result {} updated: score {}, level {}, incomplete {}",
                analysRsltId, scored.score(), scored.level().name(), incomplete);
    }

    private static boolean headerAuthFailed(Set<Indicator> fired) {
        return fired.contains(Indicator.SPF_FAIL)
                || fired.contains(Indicator.DKIM_FAIL)
                || fired.contains(Indicator.DMARC_FAIL);
    }
}
