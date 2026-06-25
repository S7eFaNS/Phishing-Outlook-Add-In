package com.bank.phishaid.analysis.repository.interfaces;

import com.bank.phishaid.initialization.entity.MailPathLst;
import com.bank.phishaid.initialization.entity.PhMail;

import java.util.UUID;

// Read-only

public interface IAnalysisHeaderReadRepo {

    PhMail findPhMail(UUID phMailId);

    MailPathLst findMailPath(UUID phMailId);
}
