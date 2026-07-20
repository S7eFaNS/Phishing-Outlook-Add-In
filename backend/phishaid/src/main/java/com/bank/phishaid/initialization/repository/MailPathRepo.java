package com.bank.phishaid.initialization.repository;

import com.bank.phishaid.initialization.entity.MailPathLst;
import com.bank.phishaid.initialization.repository.interfaces.IMailPathRepo;
import com.bank.phishaid.initialization.repository.jpaInterfaces.MailPathLstJpaRepository;

import org.springframework.stereotype.Repository;

//wrapper

@Repository
class MailPathRepo implements IMailPathRepo {

    private final MailPathLstJpaRepository jpa;

    MailPathRepo(MailPathLstJpaRepository jpa) {
        this.jpa = jpa;
    }

    //create new entry for header part with SPF, DKIM, DMARC and other relevant info from that table
    @Override
    public MailPathLst Create(MailPathLst mailPath) {
        return jpa.save(mailPath);
    }
}
