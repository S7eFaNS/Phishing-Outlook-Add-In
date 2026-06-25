package com.bank.phishaid.analysis.repository.jpaInterfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.phishaid.analysis.entity.HeaderScoreLst;

import java.util.UUID;

public interface HeaderScoreLstJpaRepo extends JpaRepository<HeaderScoreLst, UUID> {
}
