package com.bank.phishaid.analysis.repository;

import com.bank.phishaid.initialization.entity.MailPathLst;
import com.bank.phishaid.initialization.entity.PhMail;
import com.bank.phishaid.analysis.repository.interfaces.IAnalysisHeaderReadRepo;
import com.bank.phishaid.analysis.repository.jpaInterfaces.MailPathReadJpaRepo;
import com.bank.phishaid.analysis.repository.jpaInterfaces.PhMailReadJpaRepo;

import org.springframework.stereotype.Repository;

import java.util.UUID;

//wrapper — read-only 

@Repository
class AnalysisHeaderReadRepo implements IAnalysisHeaderReadRepo {

    private final PhMailReadJpaRepo phMailJpa;
    private final MailPathReadJpaRepo mailPathJpa;

    AnalysisHeaderReadRepo(PhMailReadJpaRepo phMailJpa, MailPathReadJpaRepo mailPathJpa) {
        this.phMailJpa = phMailJpa;
        this.mailPathJpa = mailPathJpa;
    }

    // Both rows are written during init and must exist by the time analysis runs.
    @Override
    public PhMail findPhMail(UUID phMailId) {
        return phMailJpa.findById(phMailId).orElseThrow(
                () -> new IllegalStateException("No phMail for id " + phMailId));
    }

    @Override
    public MailPathLst findMailPath(UUID phMailId) {
        return mailPathJpa.findByPhMail_PhMailId(phMailId).orElseThrow(
                () -> new IllegalStateException("No mailPathLst for phMail " + phMailId));
    }
}
