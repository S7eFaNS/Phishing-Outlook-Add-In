package com.bank.phishaid.analysis.repository.jpaInterfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.phishaid.initialization.entity.MailPathLst;

import java.util.Optional;
import java.util.UUID;

// Read-only 

public interface MailPathReadJpaRepo extends JpaRepository<MailPathLst, UUID> {

    Optional<MailPathLst> findByPhMail_PhMailId(UUID phMailId);
}
