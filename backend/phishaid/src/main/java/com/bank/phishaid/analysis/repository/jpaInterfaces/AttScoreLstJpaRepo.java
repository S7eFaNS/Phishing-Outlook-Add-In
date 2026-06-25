package com.bank.phishaid.analysis.repository.jpaInterfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.phishaid.analysis.entity.AttScoreLst;

import java.util.UUID;

public interface AttScoreLstJpaRepo extends JpaRepository<AttScoreLst, UUID> {
}
