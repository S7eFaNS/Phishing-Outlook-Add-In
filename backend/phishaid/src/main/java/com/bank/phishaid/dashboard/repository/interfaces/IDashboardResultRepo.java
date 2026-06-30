package com.bank.phishaid.dashboard.repository.interfaces;

import com.bank.phishaid.analysis.entity.AnalysRslt;
import com.bank.phishaid.analysis.entity.AttScoreLst;
import com.bank.phishaid.analysis.entity.HeaderScoreLst;
import com.bank.phishaid.analysis.entity.LinkScoreLst;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IDashboardResultRepo {

    Page<AnalysRslt> findAll(Pageable pageable);

    Optional<AnalysRslt> findById(UUID analysRsltId);

    Optional<AnalysRslt> findByPhMailId(UUID phMailId);

    boolean existsById(UUID analysRsltId);

    Optional<HeaderScoreLst> findHeaderScoreByResultId(UUID analysRsltId);

    List<LinkScoreLst> findLinkScoresByResultId(UUID analysRsltId);

    List<AttScoreLst> findAttScoresByResultId(UUID analysRsltId);
}
