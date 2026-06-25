package com.bank.phishaid.analysis.repository.interfaces;

import com.bank.phishaid.analysis.entity.AnalysRslt;

import java.util.UUID;

public interface IAnalysisResultRepo {

    AnalysRslt Create(AnalysRslt analysRslt, UUID phMailId);

    AnalysRslt Find(UUID analysRsltId);

    AnalysRslt Update(AnalysRslt analysRslt);
}
