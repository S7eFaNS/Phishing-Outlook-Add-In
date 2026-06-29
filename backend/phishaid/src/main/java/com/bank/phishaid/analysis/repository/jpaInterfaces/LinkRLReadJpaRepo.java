package com.bank.phishaid.analysis.repository.jpaInterfaces;

import com.bank.phishaid.initialization.entity.LinkLst;
import com.bank.phishaid.initialization.entity.LinkRL;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

// Read-only
public interface LinkRLReadJpaRepo extends JpaRepository<LinkRL, UUID> {

    // Returns the linkLst rows (id + url) so the analysis can score each and FK back to it (R6).
    @Query("select rl.linkLst from LinkRL rl where rl.phMail.phMailId = :phMailId")
    List<LinkLst> findUrlsByPhMailId(@Param("phMailId") UUID phMailId);
}
