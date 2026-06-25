package com.bank.phishaid.analysis.repository.interfaces;

import com.bank.phishaid.analysis.entity.HeaderScoreLst;

import java.util.UUID;

public interface IAnalysisHeaderRepo {

    // Persists the header score row
    HeaderScoreLst Create(HeaderScoreLst headerScore, UUID analysRsltId);
}
