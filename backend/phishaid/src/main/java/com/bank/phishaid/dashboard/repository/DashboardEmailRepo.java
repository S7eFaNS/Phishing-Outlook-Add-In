package com.bank.phishaid.dashboard.repository;

import com.bank.phishaid.dashboard.repository.interfaces.IDashboardEmailRepo;
import com.bank.phishaid.dashboard.repository.jpaInterfaces.DashboardEmailJpaRepo;
import com.bank.phishaid.initialization.entity.PhMail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

// wrapper

@Repository
class DashboardEmailRepo implements IDashboardEmailRepo {

    private final DashboardEmailJpaRepo jpa;

    DashboardEmailRepo(DashboardEmailJpaRepo jpa) {
        this.jpa = jpa;
    }

    @Override
    public Page<PhMail> findAll(Pageable pageable) {
        return jpa.findAll(pageable);
    }

    @Override
    public Optional<PhMail> findById(UUID phMailId) {
        return jpa.findById(phMailId);
    }
}
