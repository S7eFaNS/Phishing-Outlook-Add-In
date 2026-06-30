package com.bank.phishaid.dashboard.repository.interfaces;

import com.bank.phishaid.analysis.entity.AnalysRslt;
import com.bank.phishaid.initialization.entity.AttachmentLst;
import com.bank.phishaid.initialization.entity.LinkLst;
import com.bank.phishaid.initialization.entity.MailPathLst;
import com.bank.phishaid.initialization.entity.MailRelayLst;
import com.bank.phishaid.initialization.entity.PhMail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IDashboardEmailRepo {

    Page<PhMail> findAll(Pageable pageable);

    Optional<PhMail> findById(UUID phMailId);

    Optional<MailPathLst> findMailPathByPhMailId(UUID phMailId);

    List<MailRelayLst> findRelayHopsByPhMailId(UUID phMailId);

    List<LinkLst> findLinksByPhMailId(UUID phMailId);

    List<AttachmentLst> findAttachmentsByPhMailId(UUID phMailId);

    Optional<AnalysRslt> findResultByPhMailId(UUID phMailId);
}
