package com.bank.phishaid.analysis.serviceLayer.interfaces;

import com.bank.phishaid.analysis.model.Indicator;

import java.util.Set;
import java.util.UUID;

public interface IAnalysisResultService {

    //insert the aggregate result (totalScore 0, analysDesc null, frwdToHost false)
    //and return its id so the per-dimension score tables can FK to it.
    UUID Create(UUID phMailId);

    //score the union of fired indicators and write the verdict onto the result row
    void Update(UUID analysRsltId, Set<Indicator> fired, boolean incomplete);
}
