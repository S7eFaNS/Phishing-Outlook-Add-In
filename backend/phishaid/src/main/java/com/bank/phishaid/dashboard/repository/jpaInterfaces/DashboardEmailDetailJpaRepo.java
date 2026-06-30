package com.bank.phishaid.dashboard.repository.jpaInterfaces;

import com.bank.phishaid.analysis.entity.AnalysRslt;
import com.bank.phishaid.initialization.entity.AttachmentLst;
import com.bank.phishaid.initialization.entity.LinkLst;
import com.bank.phishaid.initialization.entity.MailPathLst;
import com.bank.phishaid.initialization.entity.MailRelayLst;
import com.bank.phishaid.initialization.entity.PhMail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DashboardEmailDetailJpaRepo extends JpaRepository<PhMail, UUID> {

    // The 1:1 auth header (senderIp, spf/dkim/dmarc booleans).
    @Query("select mp from MailPathLst mp where mp.phMail.phMailId = :phMailId")
    Optional<MailPathLst> findMailPathByPhMailId(@Param("phMailId") UUID phMailId);

    // Relay hops via mailRelayRL → mailRelayLst, ordered by hopNumber.
    @Query("select rl.mailRelay from MailRelayRL rl "
            + "where rl.mailPath.phMail.phMailId = :phMailId order by rl.mailRelay.hopNumber asc")
    List<MailRelayLst> findRelayHopsByPhMailId(@Param("phMailId") UUID phMailId);

    // URLs via linkRL → linkLst.
    @Query("select rl.linkLst from LinkRL rl where rl.phMail.phMailId = :phMailId")
    List<LinkLst> findLinksByPhMailId(@Param("phMailId") UUID phMailId);

    // Attachments via attachmentRL → attachmentLst.
    @Query("select rl.attLst from AttachmentRL rl where rl.phMail.phMailId = :phMailId")
    List<AttachmentLst> findAttachmentsByPhMailId(@Param("phMailId") UUID phMailId);

    // The analysis result, if the email has been analysed yet.
    @Query("select r from AnalysRslt r where r.phMail.phMailId = :phMailId")
    Optional<AnalysRslt> findResultByPhMailId(@Param("phMailId") UUID phMailId);
}
