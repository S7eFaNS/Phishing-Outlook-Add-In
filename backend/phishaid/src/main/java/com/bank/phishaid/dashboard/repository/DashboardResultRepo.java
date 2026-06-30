package com.bank.phishaid.dashboard.repository;

import com.bank.phishaid.analysis.entity.AnalysRslt;
import com.bank.phishaid.analysis.entity.AttScoreLst;
import com.bank.phishaid.analysis.entity.HeaderScoreLst;
import com.bank.phishaid.analysis.entity.LinkScoreLst;
import com.bank.phishaid.dashboard.repository.interfaces.IDashboardResultRepo;
import com.bank.phishaid.dashboard.repository.jpaInterfaces.DashboardBreakdownJpaRepo;
import com.bank.phishaid.dashboard.repository.jpaInterfaces.DashboardResultJpaRepo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// wrapper
@Repository
class DashboardResultRepo implements IDashboardResultRepo {

    private final DashboardResultJpaRepo jpa;
    private final DashboardBreakdownJpaRepo breakdownJpa;

    DashboardResultRepo(DashboardResultJpaRepo jpa, DashboardBreakdownJpaRepo breakdownJpa) {
        this.jpa = jpa;
        this.breakdownJpa = breakdownJpa;
    }

    @Override
    public Page<AnalysRslt> findAll(Pageable pageable) {
        return jpa.findAll(pageable);
    }

    @Override
    public Optional<AnalysRslt> findById(UUID analysRsltId) {
        return jpa.findById(analysRsltId);
    }

    @Override
    public Optional<AnalysRslt> findByPhMailId(UUID phMailId) {
        return jpa.findByPhMail_PhMailId(phMailId);
    }

    @Override
    public boolean existsById(UUID analysRsltId) {
        return jpa.existsById(analysRsltId);
    }

    @Override
    public Optional<HeaderScoreLst> findHeaderScoreByResultId(UUID analysRsltId) {
        return breakdownJpa.findHeaderScoreByResultId(analysRsltId);
    }

    @Override
    public List<LinkScoreLst> findLinkScoresByResultId(UUID analysRsltId) {
        return breakdownJpa.findLinkScoresByResultId(analysRsltId);
    }

    @Override
    public List<AttScoreLst> findAttScoresByResultId(UUID analysRsltId) {
        return breakdownJpa.findAttScoresByResultId(analysRsltId);
    }
}
