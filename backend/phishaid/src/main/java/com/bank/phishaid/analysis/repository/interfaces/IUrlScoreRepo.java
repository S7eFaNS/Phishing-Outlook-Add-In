package com.bank.phishaid.analysis.repository.interfaces;

import com.bank.phishaid.analysis.entity.LinkScoreLst;
import com.bank.phishaid.analysis.entity.LinkScoreRL;

import java.util.UUID;

public interface IUrlScoreRepo {

    // Persists a single per-URL score row and returns it with its generated id populated

    LinkScoreLst Create(LinkScoreLst linkScore);

    // Links a persisted linkScoreLst row to its analysRslt by inserting a row into linkScoreRL

    LinkScoreRL CreateJunctionColumn(UUID analysRsltId, LinkScoreLst linkScore);
}
