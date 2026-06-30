package com.bank.phishaid.dashboard.repository.interfaces;

import com.bank.phishaid.dashboard.dto.IpCountDto;
import com.bank.phishaid.dashboard.repository.jpaInterfaces.AnalysDescCount;
import com.bank.phishaid.dashboard.repository.jpaInterfaces.SenderFromCount;

import java.time.Instant;
import java.util.List;

public interface IDashboardStatsRepo {

    List<SenderFromCount> findSenderCountsGroupedByFrom();

    List<IpCountDto> findTopSenderIps(int limit);

    long countEmailsInWindow(Instant from, Instant to);

    List<AnalysDescCount> findResultDescCountsInWindow(Instant from, Instant to);

    long countFrwdToHostInWindow(Instant from, Instant to);
}
