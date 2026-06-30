package com.bank.phishaid.dashboard.repository;

import com.bank.phishaid.dashboard.repository.interfaces.IDashboardLinkRepo;
import com.bank.phishaid.dashboard.repository.jpaInterfaces.DashboardLinkEmailsJpaRepo;
import com.bank.phishaid.dashboard.repository.jpaInterfaces.DashboardLinkJpaRepo;
import com.bank.phishaid.initialization.entity.LinkLst;
import com.bank.phishaid.initialization.entity.PhMail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

// wrapper

@Repository
class DashboardLinkRepo implements IDashboardLinkRepo {

    private final DashboardLinkJpaRepo jpa;
    private final DashboardLinkEmailsJpaRepo emailsJpa;

    DashboardLinkRepo(DashboardLinkJpaRepo jpa, DashboardLinkEmailsJpaRepo emailsJpa) {
        this.jpa = jpa;
        this.emailsJpa = emailsJpa;
    }

    @Override
    public Page<LinkLst> findAll(Pageable pageable) {
        return jpa.findAll(pageable);
    }

    @Override
    public boolean existsById(UUID linkLstId) {
        return jpa.existsById(linkLstId);
    }

    @Override
    public Page<PhMail> findEmailsForLink(UUID linkLstId, Pageable pageable) {
        return emailsJpa.findEmailsForLink(linkLstId, pageable);
    }
}
