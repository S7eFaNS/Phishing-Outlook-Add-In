package com.bank.phishaid.dashboard.repository;

import com.bank.phishaid.analysis.entity.AnalysRslt;
import com.bank.phishaid.dashboard.repository.interfaces.IDashboardEmailRepo;
import com.bank.phishaid.dashboard.repository.jpaInterfaces.DashboardEmailDetailJpaRepo;
import com.bank.phishaid.dashboard.repository.jpaInterfaces.DashboardEmailJpaRepo;
import com.bank.phishaid.initialization.entity.AttachmentLst;
import com.bank.phishaid.initialization.entity.LinkLst;
import com.bank.phishaid.initialization.entity.MailPathLst;
import com.bank.phishaid.initialization.entity.MailRelayLst;
import com.bank.phishaid.initialization.entity.PhMail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// wrapper

@Repository
class DashboardEmailRepo implements IDashboardEmailRepo {

    private final DashboardEmailJpaRepo jpa;
    private final DashboardEmailDetailJpaRepo detailJpa;

    DashboardEmailRepo(DashboardEmailJpaRepo jpa, DashboardEmailDetailJpaRepo detailJpa) {
        this.jpa = jpa;
        this.detailJpa = detailJpa;
    }

    @Override
    public Page<PhMail> findAll(Pageable pageable) {
        return jpa.findAll(pageable);
    }

    @Override
    public Optional<PhMail> findById(UUID phMailId) {
        return jpa.findById(phMailId);
    }

    @Override
    public Optional<MailPathLst> findMailPathByPhMailId(UUID phMailId) {
        return detailJpa.findMailPathByPhMailId(phMailId);
    }

    @Override
    public List<MailRelayLst> findRelayHopsByPhMailId(UUID phMailId) {
        return detailJpa.findRelayHopsByPhMailId(phMailId);
    }

    @Override
    public List<LinkLst> findLinksByPhMailId(UUID phMailId) {
        return detailJpa.findLinksByPhMailId(phMailId);
    }

    @Override
    public List<AttachmentLst> findAttachmentsByPhMailId(UUID phMailId) {
        return detailJpa.findAttachmentsByPhMailId(phMailId);
    }

    @Override
    public Optional<AnalysRslt> findResultByPhMailId(UUID phMailId) {
        return detailJpa.findResultByPhMailId(phMailId);
    }
}
