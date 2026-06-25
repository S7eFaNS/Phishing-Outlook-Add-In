package com.bank.phishaid.analysis.serviceLayer;

import com.bank.phishaid.analysis.model.Indicator;
import com.bank.phishaid.analysis.model.ScanResult;
import com.bank.phishaid.analysis.serviceLayer.interfaces.IAnalysisAttService;
import com.bank.phishaid.analysis.serviceLayer.interfaces.IAnalysisHeaderService;
import com.bank.phishaid.analysis.serviceLayer.interfaces.IAnalysisResultService;
import com.bank.phishaid.analysis.serviceLayer.interfaces.IAnalysisUrlService;
import com.bank.phishaid.analysis.serviceLayer.interfaces.IOrchestrationAnalysisService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

// NOT @Transactional: the VirusTotal calls must not run inside an open DB transaction 
// Verdicts are gathered per dimension, then the union is scored and written once.
@Service
class OrchestrationAnalysisService implements IOrchestrationAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(OrchestrationAnalysisService.class);

    private final IAnalysisResultService resultService;
    private final IAnalysisHeaderService headerService;
    private final IAnalysisUrlService urlService;
    private final IAnalysisAttService attService;

    OrchestrationAnalysisService(IAnalysisResultService resultService,
                                 IAnalysisHeaderService headerService,
                                 IAnalysisUrlService urlService,
                                 IAnalysisAttService attService) {
        this.resultService = resultService;
        this.headerService = headerService;
        this.urlService = urlService;
        this.attService = attService;
    }

    @Override
    public void ProcessAnalysis(UUID phMailId) {
        log.info("Analysis started for phMail {}", phMailId);

        //create the aggregate result row up-front so the score tables can FK to it.
        UUID analysRsltId = resultService.Create(phMailId);

        //header authentication (local, no external call).
        Set<Indicator> headerFired = headerService.AnalysisHeader(phMailId, analysRsltId);

        //URL + attachment reputation via VirusTotal.
        ScanResult urls = urlService.AnalyseUrls(phMailId, analysRsltId);
        ScanResult atts = attService.AnalyseAtt(phMailId, analysRsltId);

        //aggregate over the union of fired indicators; incomplete if any VT step skipped.
        Set<Indicator> firedUnion = EnumSet.noneOf(Indicator.class);
        firedUnion.addAll(headerFired);
        firedUnion.addAll(urls.fired());
        firedUnion.addAll(atts.fired());
        boolean incomplete = !urls.complete() || !atts.complete();
        resultService.Update(analysRsltId, firedUnion, incomplete);

        log.info("Analysis complete for phMail {}: result {}, {} indicator(s) fired, incomplete {}",
                phMailId, analysRsltId, firedUnion.size(), incomplete);
    }
}
