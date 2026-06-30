package com.bank.phishaid.analysis.repository.interfaces;

import com.bank.phishaid.initialization.entity.LinkLst;

import java.util.List;
import java.util.UUID;

// Read-only
public interface IAnalysisUrlReadRepo {

    // The URLs stored for this email (linkRL → linkLst), carrying linkLstId for the R6 FK.
    List<LinkLst> findUrls(UUID phMailId);
}
