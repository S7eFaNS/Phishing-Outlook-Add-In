package com.bank.phishaid.analysis.repository;

import com.bank.phishaid.analysis.repository.interfaces.IAnalysisAttReadRepo;
import com.bank.phishaid.analysis.repository.jpaInterfaces.AttachmentRLReadJpaRepo;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

//wrapper — read-only 

@Repository
class AnalysisAttReadRepo implements IAnalysisAttReadRepo {

    private final AttachmentRLReadJpaRepo jpa;

    AnalysisAttReadRepo(AttachmentRLReadJpaRepo jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<String> findAttNames(UUID phMailId) {
        return jpa.findAttNamesByPhMailId(phMailId);
    }
}
