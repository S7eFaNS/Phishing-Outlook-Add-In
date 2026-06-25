package com.bank.phishaid.analysis.repository.interfaces;

import java.util.List;
import java.util.UUID;

// Read-only 
public interface IAnalysisAttReadRepo {

    // The attachment hashes (attName) stored for this email (attachmentRL → attachmentLst).
    List<String> findAttNames(UUID phMailId);
}
