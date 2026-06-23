package com.bank.phishaid.repository.initialization.jpaInterfaces;

import com.bank.phishaid.entity.PhMail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhMailJpaRepository extends JpaRepository<PhMail, UUID> {
}
