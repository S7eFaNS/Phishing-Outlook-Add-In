package com.bank.phishaid.analysis.repository.jpaInterfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.phishaid.analysis.entity.AnalysRslt;

import java.util.UUID;

public interface AnalysRsltJpaRepo extends JpaRepository<AnalysRslt, UUID> {
}
