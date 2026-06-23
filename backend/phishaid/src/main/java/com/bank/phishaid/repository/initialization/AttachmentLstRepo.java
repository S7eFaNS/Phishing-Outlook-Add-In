package com.bank.phishaid.repository.initialization;

import com.bank.phishaid.entity.AttachmentLst;
import com.bank.phishaid.entity.AttachmentRL;
import com.bank.phishaid.entity.PhMail;
import com.bank.phishaid.repository.initialization.interfaces.IAttachmentRepo;
import com.bank.phishaid.repository.initialization.jpaInterfaces.AttachmentRLJpaRepository;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.UUID;

// wrapper

@Repository
class AttachmentLstRepo implements IAttachmentRepo {

    private final EntityManager em;
    private final AttachmentRLJpaRepository attachmentRLJpa;

    AttachmentLstRepo(EntityManager em, AttachmentRLJpaRepository attachmentRLJpa) {
        this.em = em;
        this.attachmentRLJpa = attachmentRLJpa;
    }

    @Override
    public UUID Create(String attName) {
        em.createNativeQuery(
                        "INSERT INTO attachmentlst (attlstid, attname, count) "
                                + "VALUES (gen_random_uuid(), :name, 1) "
                                + "ON CONFLICT (attname) DO UPDATE SET count = attachmentlst.count + 1")
                .setParameter("name", attName)
                .executeUpdate();

        Object id = em.createNativeQuery("SELECT attlstid FROM attachmentlst WHERE attname = :name")
                .setParameter("name", attName)
                .getSingleResult();
        return toUuid(id);
    }

    @Override
    public void CreateJunctionColumn(UUID phMailId, UUID attLstId) {
        AttachmentRL link = new AttachmentRL();
        link.setPhMail(em.getReference(PhMail.class, phMailId));
        link.setAttLst(em.getReference(AttachmentLst.class, attLstId));
        attachmentRLJpa.save(link);
    }

    private static UUID toUuid(Object value) {
        return (value instanceof UUID uuid) ? uuid : UUID.fromString(value.toString());
    }
}
