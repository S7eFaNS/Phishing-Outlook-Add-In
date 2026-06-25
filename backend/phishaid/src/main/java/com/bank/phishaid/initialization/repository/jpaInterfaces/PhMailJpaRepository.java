package com.bank.phishaid.initialization.repository.jpaInterfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.phishaid.initialization.entity.PhMail;

import java.util.UUID;

public interface PhMailJpaRepository extends JpaRepository<PhMail, UUID> {
}
