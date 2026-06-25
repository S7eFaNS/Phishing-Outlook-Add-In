package com.bank.phishaid.initialization.repository.jpaInterfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.phishaid.initialization.entity.LinkRL;

import java.util.UUID;

public interface LinkRLJpaRepository extends JpaRepository<LinkRL, UUID> {
}
