package com.bank.phishaid.analysis.repository;

import com.bank.phishaid.analysis.entity.AnalysRslt;
import com.bank.phishaid.analysis.entity.LinkScoreLst;
import com.bank.phishaid.analysis.entity.LinkScoreRL;
import com.bank.phishaid.analysis.repository.interfaces.IUrlScoreRepo;
import com.bank.phishaid.analysis.repository.jpaInterfaces.LinkScoreLstJpaRepo;
import com.bank.phishaid.analysis.repository.jpaInterfaces.LinkScoreRLJpaRepo;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

//wrapper

@Repository
class UrlScoreRepo implements IUrlScoreRepo {

    private final EntityManager em;
    private final LinkScoreLstJpaRepo scoreJpa;
    private final LinkScoreRLJpaRepo junctionJpa;

    UrlScoreRepo(EntityManager em, LinkScoreLstJpaRepo scoreJpa, LinkScoreRLJpaRepo junctionJpa) {
        this.em = em;
        this.scoreJpa = scoreJpa;
        this.junctionJpa = junctionJpa;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public LinkScoreLst Create(LinkScoreLst linkScore) {
        return scoreJpa.save(linkScore);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public LinkScoreRL CreateJunctionColumn(UUID analysRsltId, LinkScoreLst linkScore) {
        LinkScoreRL link = new LinkScoreRL();
        link.setAnalysRslt(em.getReference(AnalysRslt.class, analysRsltId));
        link.setLinkScoreLst(linkScore);
        return junctionJpa.save(link);
    }
}
