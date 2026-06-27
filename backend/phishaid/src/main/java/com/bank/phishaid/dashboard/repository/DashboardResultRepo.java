package com.bank.phishaid.dashboard.repository;

import com.bank.phishaid.analysis.entity.AnalysRslt;
import com.bank.phishaid.dashboard.repository.interfaces.IDashboardResultRepo;
import com.bank.phishaid.dashboard.repository.jpaInterfaces.DashboardResultJpaRepo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

// wrapper 
@Repository
class DashboardResultRepo implements IDashboardResultRepo {

    private final DashboardResultJpaRepo jpa;

    DashboardResultRepo(DashboardResultJpaRepo jpa) {
        this.jpa = jpa;
    }

    @Override
    public Page<AnalysRslt> findAll(Pageable pageable) {
        return jpa.findAll(pageable);
    }

    @Override
    public Optional<AnalysRslt> findById(UUID analysRsltId) {
        return jpa.findById(analysRsltId);
    }
}
