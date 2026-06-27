package com.bank.phishaid.dashboard.repository.interfaces;

import com.bank.phishaid.analysis.entity.AnalysRslt;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface IDashboardResultRepo {

    Page<AnalysRslt> findAll(Pageable pageable);

    Optional<AnalysRslt> findById(UUID analysRsltId);
}
