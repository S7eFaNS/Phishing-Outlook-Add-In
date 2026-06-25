package com.bank.phishaid.initialization.serviceLayer.interfaces;

import com.bank.phishaid.initialization.dto.EmailHeaderDTO;
import com.bank.phishaid.initialization.entity.MailPathLst;
import com.bank.phishaid.initialization.entity.MailRelayLst;
import com.bank.phishaid.initialization.entity.PhMail;

import java.util.List;

public interface IHeaderInitService {

    //creates phMail from the header
    PhMail InitPhMail(EmailHeaderDTO head);

    //Builds and persists the 1:1 mailPathLst summary
    MailPathLst InitMailPath(EmailHeaderDTO head, PhMail phMail);

    //persists each relay hop
    List<MailRelayLst> InitMailRelay(EmailHeaderDTO head, MailPathLst mailPath);
}
