package com.bank.phishaid.serviceLayer.initialization.interfaces;

import com.bank.phishaid.dto.EmailHeaderDTO;
import com.bank.phishaid.entity.MailPathLst;
import com.bank.phishaid.entity.MailRelayLst;
import com.bank.phishaid.entity.PhMail;

import java.util.List;

public interface IHeaderInitService {

    //creates phMail from the header
    PhMail InitPhMail(EmailHeaderDTO head);

    //Builds and persists the 1:1 mailPathLst summary
    MailPathLst InitMailPath(EmailHeaderDTO head, PhMail phMail);

    //persists each relay hop
    List<MailRelayLst> InitMailRelay(EmailHeaderDTO head, MailPathLst mailPath);
}
