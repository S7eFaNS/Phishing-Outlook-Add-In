package com.bank.phishaid.serviceLayer.analysis;

import java.util.UUID;

/**
 * Analysis-phase seam (§6). Declared only — there is no implementation yet and the init
 * orchestrator does NOT call it. It marks where the scoring hand-off (VirusTotal + header
 * auth checks) will attach once the analysis module is built. Do not wire or invoke it now.
 */
public interface IOrchestrationAnalysisService {

    /** Future hand-off: start analysis/scoring for an initialised phMail. */
    void StartAnalysis(UUID phMailId);
}
