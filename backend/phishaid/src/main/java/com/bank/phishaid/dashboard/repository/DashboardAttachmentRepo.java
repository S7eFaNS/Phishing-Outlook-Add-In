package com.bank.phishaid.dashboard.repository;

import com.bank.phishaid.dashboard.repository.interfaces.IDashboardAttachmentRepo;
import com.bank.phishaid.dashboard.repository.jpaInterfaces.DashboardAttachmentEmailsJpaRepo;
import com.bank.phishaid.dashboard.repository.jpaInterfaces.DashboardAttachmentJpaRepo;
import com.bank.phishaid.initialization.entity.AttachmentLst;
import com.bank.phishaid.initialization.entity.PhMail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

// wrapper

@Repository
class DashboardAttachmentRepo implements IDashboardAttachmentRepo {

    private final DashboardAttachmentJpaRepo jpa;
    private final DashboardAttachmentEmailsJpaRepo emailsJpa;

    DashboardAttachmentRepo(DashboardAttachmentJpaRepo jpa, DashboardAttachmentEmailsJpaRepo emailsJpa) {
        this.jpa = jpa;
        this.emailsJpa = emailsJpa;
    }

    @Override
    public Page<AttachmentLst> findAll(Pageable pageable) {
        return jpa.findAll(pageable);
    }

    @Override
    public boolean existsById(UUID attLstId) {
        return jpa.existsById(attLstId);
    }

    @Override
    public Page<PhMail> findEmailsForAttachment(UUID attLstId, Pageable pageable) {
        return emailsJpa.findEmailsForAttachment(attLstId, pageable);
    }
}
