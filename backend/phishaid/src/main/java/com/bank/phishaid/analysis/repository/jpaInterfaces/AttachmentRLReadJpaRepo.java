package com.bank.phishaid.analysis.repository.jpaInterfaces;

import com.bank.phishaid.initialization.entity.AttachmentLst;
import com.bank.phishaid.initialization.entity.AttachmentRL;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

// Read-only
public interface AttachmentRLReadJpaRepo extends JpaRepository<AttachmentRL, UUID> {

    // Returns the attachmentLst rows (id + hash) so the analysis can score each and FK back to it (R6).
    @Query("select rl.attLst from AttachmentRL rl where rl.phMail.phMailId = :phMailId")
    List<AttachmentLst> findAttNamesByPhMailId(@Param("phMailId") UUID phMailId);
}
