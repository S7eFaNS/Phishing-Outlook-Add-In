package com.bank.phishaid.analysis.repository.jpaInterfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.phishaid.analysis.entity.LinkScoreLst;

import java.util.UUID;

public interface LinkScoreLstJpaRepo extends JpaRepository<LinkScoreLst, UUID> {
}
