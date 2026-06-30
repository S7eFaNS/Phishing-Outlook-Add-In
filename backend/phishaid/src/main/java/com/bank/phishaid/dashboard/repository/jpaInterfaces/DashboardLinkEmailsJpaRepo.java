package com.bank.phishaid.dashboard.repository.jpaInterfaces;

import com.bank.phishaid.initialization.entity.PhMail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DashboardLinkEmailsJpaRepo extends JpaRepository<PhMail, UUID> {

    @Query("select p from PhMail p where exists "
            + "(select 1 from LinkRL rl where rl.phMail = p and rl.linkLst.linkLstId = :linkLstId)")
    Page<PhMail> findEmailsForLink(@Param("linkLstId") UUID linkLstId, Pageable pageable);
}
