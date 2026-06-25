package com.bank.phishaid.analysis.repository;

import com.bank.phishaid.analysis.entity.AnalysRslt;
import com.bank.phishaid.initialization.entity.PhMail;
import com.bank.phishaid.analysis.repository.interfaces.IAnalysisResultRepo;
import com.bank.phishaid.analysis.repository.jpaInterfaces.AnalysRsltJpaRepo;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.UUID;

//wrapper

@Repository
class AnalysisResultRepo implements IAnalysisResultRepo {

    private final EntityManager em;
    private final AnalysRsltJpaRepo jpa;

    AnalysisResultRepo(EntityManager em, AnalysRsltJpaRepo jpa) {
        this.em = em;
        this.jpa = jpa;
    }

    @Override
    public AnalysRslt Create(AnalysRslt analysRslt, UUID phMailId) {
        analysRslt.setPhMail(em.getReference(PhMail.class, phMailId));
        return jpa.save(analysRslt);
    }

    @Override
    public AnalysRslt Find(UUID analysRsltId) {
        return jpa.findById(analysRsltId).orElseThrow(
                () -> new IllegalStateException("No analysRslt for id " + analysRsltId));
    }

    @Override
    public AnalysRslt Update(AnalysRslt analysRslt) {
        return jpa.save(analysRslt);
    }
}
