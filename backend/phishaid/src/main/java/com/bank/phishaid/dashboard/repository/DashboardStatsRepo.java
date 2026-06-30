package com.bank.phishaid.dashboard.repository;

import com.bank.phishaid.dashboard.dto.IpCountDto;
import com.bank.phishaid.dashboard.repository.interfaces.IDashboardStatsRepo;
import com.bank.phishaid.dashboard.repository.jpaInterfaces.AnalysDescCount;
import com.bank.phishaid.dashboard.repository.jpaInterfaces.DashboardStatsJpaRepo;
import com.bank.phishaid.dashboard.repository.jpaInterfaces.SenderFromCount;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

// wrapper

@Repository
class DashboardStatsRepo implements IDashboardStatsRepo {

    private final DashboardStatsJpaRepo jpa;

    DashboardStatsRepo(DashboardStatsJpaRepo jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<SenderFromCount> findSenderCountsGroupedByFrom() {
        return jpa.findSenderCountsGroupedByFrom();
    }

    @Override
    public List<IpCountDto> findTopSenderIps(int limit) {
        return jpa.findTopSenderIps(PageRequest.of(0, limit));
    }

    @Override
    public long countEmailsInWindow(Instant from, Instant to) {
        return jpa.countEmailsInWindow(from, to);
    }

    @Override
    public List<AnalysDescCount> findResultDescCountsInWindow(Instant from, Instant to) {
        return jpa.findResultDescCountsInWindow(from, to);
    }

    @Override
    public long countFrwdToHostInWindow(Instant from, Instant to) {
        return jpa.countFrwdToHostInWindow(from, to);
    }
}
