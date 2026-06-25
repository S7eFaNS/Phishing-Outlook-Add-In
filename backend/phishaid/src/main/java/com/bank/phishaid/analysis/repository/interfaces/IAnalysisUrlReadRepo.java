package com.bank.phishaid.analysis.repository.interfaces;

import java.util.List;
import java.util.UUID;

// Read-only 
public interface IAnalysisUrlReadRepo {

    // The URLs stored for this email (linkRL → linkLst).
    List<String> findUrls(UUID phMailId);
}
