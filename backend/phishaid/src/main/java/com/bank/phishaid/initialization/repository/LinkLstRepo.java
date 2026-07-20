package com.bank.phishaid.initialization.repository;

import com.bank.phishaid.initialization.entity.LinkLst;
import com.bank.phishaid.initialization.entity.LinkRL;
import com.bank.phishaid.initialization.entity.PhMail;
import com.bank.phishaid.initialization.repository.interfaces.ILinkRepo;
import com.bank.phishaid.initialization.repository.jpaInterfaces.LinkRLJpaRepository;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.UUID;

//wrapper

@Repository
class LinkLstRepo implements ILinkRepo {

    private final EntityManager em;
    private final LinkRLJpaRepository linkRLJpa;

    LinkLstRepo(EntityManager em, LinkRLJpaRepository linkRLJpa) {
        this.em = em;
        this.linkRLJpa = linkRLJpa;
    }

    //increment count if already existing, otherwise create unique url entry
    @Override
    public UUID Create(String linkUrl) {

        em.createNativeQuery(
                        "INSERT INTO linklst (linklstid, linkurl, count) "
                                + "VALUES (gen_random_uuid(), :url, 1) "
                                + "ON CONFLICT (linkurl) DO UPDATE SET count = linklst.count + 1")
                .setParameter("url", linkUrl)
                .executeUpdate();

        Object id = em.createNativeQuery("SELECT linklstid FROM linklst WHERE linkurl = :url")
                .setParameter("url", linkUrl)
                .getSingleResult();
        return toUuid(id);
    }

    //create a joint table for phMail and linkLst with corresponding ids
    @Override
    public void CreateJunctionColumn(UUID phMailId, UUID linkLstId) {
        LinkRL link = new LinkRL();
        link.setPhMail(em.getReference(PhMail.class, phMailId));
        link.setLinkLst(em.getReference(LinkLst.class, linkLstId));
        linkRLJpa.save(link);
    }

    private static UUID toUuid(Object value) {
        return (value instanceof UUID uuid) ? uuid : UUID.fromString(value.toString());
    }
}
