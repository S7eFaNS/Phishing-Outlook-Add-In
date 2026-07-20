package com.bank.phishaid.initialization.repository;

import com.bank.phishaid.initialization.entity.MailPathLst;
import com.bank.phishaid.initialization.entity.MailRelayLst;
import com.bank.phishaid.initialization.entity.MailRelayRL;
import com.bank.phishaid.initialization.repository.interfaces.IMailRelayRepo;
import com.bank.phishaid.initialization.repository.jpaInterfaces.MailRelayLstJpaRepository;
import com.bank.phishaid.initialization.repository.jpaInterfaces.MailRelayRLJpaRepository;

import org.springframework.stereotype.Repository;

//wrapper

@Repository
class MailRelayRepo implements IMailRelayRepo {

    private final MailRelayLstJpaRepository relayJpa;
    private final MailRelayRLJpaRepository junctionJpa;

    MailRelayRepo(MailRelayLstJpaRepository relayJpa, MailRelayRLJpaRepository junctionJpa) {
        this.relayJpa = relayJpa;
        this.junctionJpa = junctionJpa;
    }

    //create mail relay hops
    @Override
    public MailRelayLst Create(MailRelayLst mailRelay) {
        return relayJpa.save(mailRelay);
    }

    //create connection between mail relay hops and mailPath table
    @Override
    public MailRelayRL CreateJunctionColumn(MailPathLst mailPath, MailRelayLst mailRelay) {
        MailRelayRL link = new MailRelayRL();
        link.setMailPath(mailPath);
        link.setMailRelay(mailRelay);
        return junctionJpa.save(link);
    }
}
