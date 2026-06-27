package com.bank.phishaid.dashboard.repository;

import com.bank.phishaid.dashboard.repository.interfaces.IDashboardAttachmentRepo;
import com.bank.phishaid.dashboard.repository.jpaInterfaces.DashboardAttachmentJpaRepo;
import com.bank.phishaid.initialization.entity.AttachmentLst;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

// wrapper

@Repository
class DashboardAttachmentRepo implements IDashboardAttachmentRepo {

    private final DashboardAttachmentJpaRepo jpa;

    DashboardAttachmentRepo(DashboardAttachmentJpaRepo jpa) {
        this.jpa = jpa;
    }

    @Override
    public Page<AttachmentLst> findAll(Pageable pageable) {
        return jpa.findAll(pageable);
    }
}
