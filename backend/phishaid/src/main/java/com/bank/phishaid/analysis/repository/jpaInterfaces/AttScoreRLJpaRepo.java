package com.bank.phishaid.analysis.repository.jpaInterfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.phishaid.analysis.entity.AttScoreRL;

import java.util.UUID;

public interface AttScoreRLJpaRepo extends JpaRepository<AttScoreRL, UUID> {
}
