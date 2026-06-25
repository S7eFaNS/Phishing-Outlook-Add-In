package com.bank.phishaid.analysis.repository.jpaInterfaces;

import com.bank.phishaid.initialization.entity.AttachmentRL;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

// Read-only 
public interface AttachmentRLReadJpaRepo extends JpaRepository<AttachmentRL, UUID> {

    @Query("select rl.attLst.attName from AttachmentRL rl where rl.phMail.phMailId = :phMailId")
    List<String> findAttNamesByPhMailId(@Param("phMailId") UUID phMailId);
}
