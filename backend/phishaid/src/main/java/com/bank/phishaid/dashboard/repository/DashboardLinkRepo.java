package com.bank.phishaid.dashboard.repository;

import com.bank.phishaid.dashboard.repository.interfaces.IDashboardLinkRepo;
import com.bank.phishaid.dashboard.repository.jpaInterfaces.DashboardLinkJpaRepo;
import com.bank.phishaid.initialization.entity.LinkLst;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

// wrapper

@Repository
class DashboardLinkRepo implements IDashboardLinkRepo {

    private final DashboardLinkJpaRepo jpa;

    DashboardLinkRepo(DashboardLinkJpaRepo jpa) {
        this.jpa = jpa;
    }

    @Override
    public Page<LinkLst> findAll(Pageable pageable) {
        return jpa.findAll(pageable);
    }
}
