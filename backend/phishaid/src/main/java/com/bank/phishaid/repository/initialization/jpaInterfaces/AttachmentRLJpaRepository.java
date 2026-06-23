package com.bank.phishaid.repository.initialization.jpaInterfaces;

import com.bank.phishaid.entity.AttachmentRL;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttachmentRLJpaRepository extends JpaRepository<AttachmentRL, UUID> {
}
