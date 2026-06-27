package com.bank.phishaid.dashboard.repository.jpaInterfaces;

import com.bank.phishaid.initialization.entity.AttachmentLst;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DashboardAttachmentJpaRepo extends JpaRepository<AttachmentLst, UUID> {
}
