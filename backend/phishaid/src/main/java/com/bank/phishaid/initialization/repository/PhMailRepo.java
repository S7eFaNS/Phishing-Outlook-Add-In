package com.bank.phishaid.initialization.repository;

import com.bank.phishaid.initialization.entity.PhMail;
import com.bank.phishaid.initialization.repository.interfaces.IPhMailRepo;
import com.bank.phishaid.initialization.repository.jpaInterfaces.PhMailJpaRepository;

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
