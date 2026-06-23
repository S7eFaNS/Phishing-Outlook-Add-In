package com.bank.phishaid.repository.initialization.jpaInterfaces;

import com.bank.phishaid.entity.MailRelayRL;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MailRelayRLJpaRepository extends JpaRepository<MailRelayRL, UUID> {
}
