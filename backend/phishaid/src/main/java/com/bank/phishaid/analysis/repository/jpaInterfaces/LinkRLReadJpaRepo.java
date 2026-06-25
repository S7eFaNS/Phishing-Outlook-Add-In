package com.bank.phishaid.analysis.repository.jpaInterfaces;

import com.bank.phishaid.initialization.entity.LinkRL;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

// Read-only 
public interface LinkRLReadJpaRepo extends JpaRepository<LinkRL, UUID> {

    @Query("select rl.linkLst.linkUrl from LinkRL rl where rl.phMail.phMailId = :phMailId")
    List<String> findUrlsByPhMailId(@Param("phMailId") UUID phMailId);
}
