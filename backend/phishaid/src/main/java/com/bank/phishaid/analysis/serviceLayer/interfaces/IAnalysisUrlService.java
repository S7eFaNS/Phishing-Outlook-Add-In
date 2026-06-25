package com.bank.phishaid.analysis.serviceLayer.interfaces;

import com.bank.phishaid.analysis.model.ScanResult;

import java.util.UUID;

public interface IAnalysisUrlService {

    // Scores each of this email's URLs via VirusTotal, writes one linkScoreLst row + junction per URL
    ScanResult AnalyseUrls(UUID phMailId, UUID analysRsltId);
}
