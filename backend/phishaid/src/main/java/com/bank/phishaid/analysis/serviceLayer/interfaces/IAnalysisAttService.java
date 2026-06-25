package com.bank.phishaid.analysis.serviceLayer.interfaces;

import com.bank.phishaid.analysis.model.ScanResult;

import java.util.UUID;

public interface IAnalysisAttService {

    // Scores each of this email's attachment hashes via VirusTotal, writes one attScoreLst row + junction per attachment
    ScanResult AnalyseAtt(UUID phMailId, UUID analysRsltId);
}
