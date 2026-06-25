package com.bank.phishaid.analysis.serviceLayer.interfaces;

import java.util.UUID;

public interface IOrchestrationAnalysisService {

    //Run analysis/scoring for an initialised phMail.
    void ProcessAnalysis(UUID phMailId);
}
