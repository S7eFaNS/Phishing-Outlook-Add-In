package com.bank.phishaid.initialization.repository.jpaInterfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.phishaid.initialization.entity.MailRelayLst;

import java.util.UUID;

public interface MailRelayLstJpaRepository extends JpaRepository<MailRelayLst, UUID> {
}
