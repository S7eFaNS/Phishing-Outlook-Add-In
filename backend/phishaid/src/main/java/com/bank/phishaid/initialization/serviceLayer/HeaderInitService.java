package com.bank.phishaid.initialization.serviceLayer;

import com.bank.phishaid.initialization.dto.EmailHeaderDTO;
import com.bank.phishaid.initialization.dto.RelayHopDTO;
import com.bank.phishaid.initialization.entity.MailPathLst;
import com.bank.phishaid.initialization.entity.MailRelayLst;
import com.bank.phishaid.initialization.entity.PhMail;
import com.bank.phishaid.initialization.repository.interfaces.IMailPathRepo;
import com.bank.phishaid.initialization.repository.interfaces.IMailRelayRepo;
import com.bank.phishaid.initialization.repository.interfaces.IPhMailRepo;
import com.bank.phishaid.initialization.serviceLayer.interfaces.IHeaderInitService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
class HeaderInitService implements IHeaderInitService {

    private static final Logger log = LoggerFactory.getLogger(HeaderInitService.class);

    private final IPhMailRepo phMailRepo;
    private final IMailPathRepo mailPathRepo;
    private final IMailRelayRepo mailRelayRepo;

    HeaderInitService(IPhMailRepo phMailRepo, IMailPathRepo mailPathRepo, IMailRelayRepo mailRelayRepo) {
        this.phMailRepo = phMailRepo;
        this.mailPathRepo = mailPathRepo;
        this.mailRelayRepo = mailRelayRepo;
    }

    //initialization of PhMail table in db
    @Override
    public PhMail InitPhMail(EmailHeaderDTO head) {
        PhMail phMail = new PhMail();
        phMail.setPhFrom(head.getPhFrom());
        phMail.setRcpt(head.getRcpt());
        phMail.setSub(head.getSub());
        phMail.setRepTo(head.getRepTo());
        phMail.setRetPath(head.getRetPath());
        phMail.setTimestampMail(head.getTimestampMail());

        PhMail saved = phMailRepo.Create(phMail);
        log.debug("Persisted phMail {}", saved.getPhMailId());
        return saved;
    }

    //init MailPath in db
    @Override
    public MailPathLst InitMailPath(EmailHeaderDTO head, PhMail phMail) {
        MailPathLst mailPath = new MailPathLst();
        mailPath.setPhMail(phMail);
        mailPath.setSenderIp(head.getSenderIp());
        mailPath.setMailSpf(head.getMailSpf());
        mailPath.setMailDkim(head.getMailDkim());
        mailPath.setMailDmarc(head.getMailDmarc());

        MailPathLst saved = mailPathRepo.Create(mailPath);
        log.debug("Persisted mailPathLst {} for phMail {}", saved.getRcvMailPathId(), phMail.getPhMailId());
        return saved;
    }

    //init Mail Relay in db
    @Override
    public List<MailRelayLst> InitMailRelay(EmailHeaderDTO head, MailPathLst mailPath) {
        List<MailRelayLst> persisted = new ArrayList<>();
        List<RelayHopDTO> relayChain = head.getRelayChain();
        if (relayChain == null) {
            return persisted;
        }

        //One row per hop, linked through mailRelayRL
        for (RelayHopDTO hop : relayChain) {
            MailRelayLst relay = new MailRelayLst();
            relay.setHopNumber(hop.getHopNumber());
            relay.setHopDescription(hop.getHopDescription());

            MailRelayLst savedRelay = mailRelayRepo.Create(relay);
            mailRelayRepo.CreateJunctionColumn(mailPath, savedRelay);
            persisted.add(savedRelay);
            log.debug("Persisted relay hop {} ({}) linked to mailPath {}",
                    savedRelay.getHopNumber(), savedRelay.getMailRelayId(), mailPath.getRcvMailPathId());
        }
        return persisted;
    }
}
