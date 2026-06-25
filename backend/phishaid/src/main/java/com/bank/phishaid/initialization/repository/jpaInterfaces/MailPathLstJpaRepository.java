package com.bank.phishaid.initialization.repository.jpaInterfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.phishaid.initialization.entity.MailPathLst;

import java.util.UUID;

public interface MailPathLstJpaRepository extends JpaRepository<MailPathLst, UUID> {
}
