package com.bank.phishaid.repository.initialization.jpaInterfaces;

import com.bank.phishaid.entity.MailRelayLst;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MailRelayLstJpaRepository extends JpaRepository<MailRelayLst, UUID> {
}
