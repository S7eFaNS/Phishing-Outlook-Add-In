package com.bank.phishaid.initialization.repository.jpaInterfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.phishaid.initialization.entity.AttachmentRL;

import java.util.UUID;

public interface AttachmentRLJpaRepository extends JpaRepository<AttachmentRL, UUID> {
}
