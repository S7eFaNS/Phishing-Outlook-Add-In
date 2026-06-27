package com.bank.phishaid.dashboard.repository.jpaInterfaces;

import com.bank.phishaid.initialization.entity.LinkLst;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DashboardLinkJpaRepo extends JpaRepository<LinkLst, UUID> {
}
