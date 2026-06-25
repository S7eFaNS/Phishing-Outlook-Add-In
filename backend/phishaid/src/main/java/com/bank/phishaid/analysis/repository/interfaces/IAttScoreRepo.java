package com.bank.phishaid.analysis.repository.interfaces;

import com.bank.phishaid.analysis.entity.AttScoreLst;
import com.bank.phishaid.analysis.entity.AttScoreRL;

import java.util.UUID;

public interface IAttScoreRepo {

    // Persists a single per-attachment score row and returns it with its generated id populated

    AttScoreLst Create(AttScoreLst attScore);

    // Links a persisted attScoreLst row to its analysRslt by inserting a row into attScoreRL

    AttScoreRL CreateJunctionColumn(UUID analysRsltId, AttScoreLst attScore);
}
