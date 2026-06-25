package com.bank.phishaid.initialization.repository.jpaInterfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.phishaid.initialization.entity.MailRelayRL;

import java.util.UUID;

public interface MailRelayRLJpaRepository extends JpaRepository<MailRelayRL, UUID> {
}
