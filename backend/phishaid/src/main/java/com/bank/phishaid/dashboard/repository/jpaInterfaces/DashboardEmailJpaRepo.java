package com.bank.phishaid.dashboard.repository.jpaInterfaces;

import com.bank.phishaid.initialization.entity.PhMail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DashboardEmailJpaRepo extends JpaRepository<PhMail, UUID> {
}
