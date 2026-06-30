package com.bank.phishaid.analysis.repository;

import com.bank.phishaid.analysis.repository.interfaces.IAnalysisUrlReadRepo;
import com.bank.phishaid.analysis.repository.jpaInterfaces.LinkRLReadJpaRepo;
import com.bank.phishaid.initialization.entity.LinkLst;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

//wrapper — read-only

@Repository
class AnalysisUrlReadRepo implements IAnalysisUrlReadRepo {

    private final LinkRLReadJpaRepo jpa;

    AnalysisUrlReadRepo(LinkRLReadJpaRepo jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<LinkLst> findUrls(UUID phMailId) {
        return jpa.findUrlsByPhMailId(phMailId);
    }
}
