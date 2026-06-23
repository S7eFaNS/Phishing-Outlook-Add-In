package com.bank.phishaid.repository.initialization;

import com.bank.phishaid.entity.PhMail;
import com.bank.phishaid.repository.initialization.interfaces.IPhMailRepo;
import com.bank.phishaid.repository.initialization.jpaInterfaces.PhMailJpaRepository;

import org.springframework.stereotype.Repository;

//wrapper

@Repository
class PhMailRepo implements IPhMailRepo {

    private final PhMailJpaRepository jpa;

    PhMailRepo(PhMailJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public PhMail Create(PhMail phMail) {
        return jpa.save(phMail);
    }
}
