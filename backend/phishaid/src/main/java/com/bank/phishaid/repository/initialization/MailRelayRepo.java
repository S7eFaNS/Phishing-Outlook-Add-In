package com.bank.phishaid.repository.initialization;

import com.bank.phishaid.entity.MailPathLst;
import com.bank.phishaid.entity.MailRelayLst;
import com.bank.phishaid.entity.MailRelayRL;
import com.bank.phishaid.repository.initialization.interfaces.IMailRelayRepo;
import com.bank.phishaid.repository.initialization.jpaInterfaces.MailRelayLstJpaRepository;
import com.bank.phishaid.repository.initialization.jpaInterfaces.MailRelayRLJpaRepository;

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

    @Override
    public MailRelayLst Create(MailRelayLst mailRelay) {
        return relayJpa.save(mailRelay);
    }

    @Override
    public MailRelayRL CreateJunctionColumn(MailPathLst mailPath, MailRelayLst mailRelay) {
        MailRelayRL link = new MailRelayRL();
        link.setMailPath(mailPath);
        link.setMailRelay(mailRelay);
        return junctionJpa.save(link);
    }
}
