package com.bank.phishaid.analysis.repository.jpaInterfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.phishaid.initialization.entity.PhMail;

import java.util.UUID;

// Read-only 

public interface PhMailReadJpaRepo extends JpaRepository<PhMail, UUID> {
}
