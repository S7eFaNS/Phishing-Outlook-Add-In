package com.bank.phishaid.analysis.repository;

import com.bank.phishaid.analysis.entity.AnalysRslt;
import com.bank.phishaid.analysis.entity.HeaderScoreLst;
import com.bank.phishaid.analysis.repository.interfaces.IAnalysisHeaderRepo;
import com.bank.phishaid.analysis.repository.jpaInterfaces.HeaderScoreLstJpaRepo;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.UUID;

//wrapper

@Repository
class AnalysisHeaderRepo implements IAnalysisHeaderRepo {

    private final EntityManager em;
    private final HeaderScoreLstJpaRepo jpa;

    AnalysisHeaderRepo(EntityManager em, HeaderScoreLstJpaRepo jpa) {
        this.em = em;
        this.jpa = jpa;
    }

    @Override
    public HeaderScoreLst Create(HeaderScoreLst headerScore, UUID analysRsltId) {
        headerScore.setAnalysRslt(em.getReference(AnalysRslt.class, analysRsltId));
        return jpa.save(headerScore);
    }
}
