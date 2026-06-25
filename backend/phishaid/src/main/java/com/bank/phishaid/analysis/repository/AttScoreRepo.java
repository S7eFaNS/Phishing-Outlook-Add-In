package com.bank.phishaid.analysis.repository;

import com.bank.phishaid.analysis.entity.AnalysRslt;
import com.bank.phishaid.analysis.entity.AttScoreLst;
import com.bank.phishaid.analysis.entity.AttScoreRL;
import com.bank.phishaid.analysis.repository.interfaces.IAttScoreRepo;
import com.bank.phishaid.analysis.repository.jpaInterfaces.AttScoreLstJpaRepo;
import com.bank.phishaid.analysis.repository.jpaInterfaces.AttScoreRLJpaRepo;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

//wrapper

@Repository
class AttScoreRepo implements IAttScoreRepo {

    private final EntityManager em;
    private final AttScoreLstJpaRepo scoreJpa;
    private final AttScoreRLJpaRepo junctionJpa;

    AttScoreRepo(EntityManager em, AttScoreLstJpaRepo scoreJpa, AttScoreRLJpaRepo junctionJpa) {
        this.em = em;
        this.scoreJpa = scoreJpa;
        this.junctionJpa = junctionJpa;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AttScoreLst Create(AttScoreLst attScore) {
        return scoreJpa.save(attScore);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AttScoreRL CreateJunctionColumn(UUID analysRsltId, AttScoreLst attScore) {
        AttScoreRL link = new AttScoreRL();
        link.setAnalysRslt(em.getReference(AnalysRslt.class, analysRsltId));
        link.setAttScoreLst(attScore);
        return junctionJpa.save(link);
    }
}
