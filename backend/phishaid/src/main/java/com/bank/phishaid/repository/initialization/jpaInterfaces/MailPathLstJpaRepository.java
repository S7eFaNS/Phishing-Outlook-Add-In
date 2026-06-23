package com.bank.phishaid.repository.initialization.jpaInterfaces;

import com.bank.phishaid.entity.MailPathLst;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MailPathLstJpaRepository extends JpaRepository<MailPathLst, UUID> {
}
