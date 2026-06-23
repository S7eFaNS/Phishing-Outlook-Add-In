package com.bank.phishaid.repository.initialization;

import com.bank.phishaid.entity.MailPathLst;
import com.bank.phishaid.repository.initialization.interfaces.IMailPathRepo;
import com.bank.phishaid.repository.initialization.jpaInterfaces.MailPathLstJpaRepository;

import org.springframework.stereotype.Repository;

//wrapper

@Repository
class MailPathRepo implements IMailPathRepo {

    private final MailPathLstJpaRepository jpa;

    MailPathRepo(MailPathLstJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public MailPathLst Create(MailPathLst mailPath) {
        return jpa.save(mailPath);
    }
}
