package com.bank.phishaid.analysis.serviceLayer.interfaces;

import com.bank.phishaid.analysis.model.Indicator;

import java.util.Set;
import java.util.UUID;

public interface IAnalysisHeaderService {

    // Scores the header dimension, writes one headerScoreLst row, and returns the fired signals.
    Set<Indicator> AnalysisHeader(UUID phMailId, UUID analysRsltId);
}
