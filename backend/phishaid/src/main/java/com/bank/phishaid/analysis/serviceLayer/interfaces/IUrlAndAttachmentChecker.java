package com.bank.phishaid.analysis.serviceLayer.interfaces;

import com.bank.phishaid.analysis.dto.Verdict;

// VirusTotal check
public interface IUrlAndAttachmentChecker {

    // Reputation of a single URL via VirusTotal's URL endpoint.
    Verdict UrlCheck(String url);

    // Reputation of a single file via VirusTotal's file (hash) endpoint.
    Verdict AttCheck(String att);
}
