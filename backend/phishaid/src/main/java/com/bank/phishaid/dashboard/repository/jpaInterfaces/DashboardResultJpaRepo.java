package com.bank.phishaid.dashboard.repository.jpaInterfaces;

import com.bank.phishaid.analysis.entity.AnalysRslt;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DashboardResultJpaRepo extends JpaRepository<AnalysRslt, UUID> {
}
