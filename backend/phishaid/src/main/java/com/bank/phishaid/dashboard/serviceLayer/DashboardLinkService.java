package com.bank.phishaid.dashboard.serviceLayer;

import com.bank.phishaid.dashboard.dto.EmailDto;
import com.bank.phishaid.dashboard.dto.LinkDto;
import com.bank.phishaid.dashboard.repository.interfaces.IDashboardLinkRepo;
import com.bank.phishaid.dashboard.serviceLayer.interfaces.IDashboardLinkService;
import com.bank.phishaid.initialization.entity.LinkLst;
import com.bank.phishaid.initialization.entity.PhMail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
class DashboardLinkService implements IDashboardLinkService {

    private final IDashboardLinkRepo repo;

    DashboardLinkService(IDashboardLinkRepo repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LinkDto> getAll(Pageable pageable) {
        return repo.findAll(pageable).map(DashboardLinkService::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmailDto> getEmailsForLink(UUID linkLstId, Pageable pageable) {
        if (!repo.existsById(linkLstId)) {
            throw new NoSuchElementException("link " + linkLstId + " not found");
        }
        return repo.findEmailsForLink(linkLstId, pageable).map(DashboardLinkService::toEmailDto);
    }

    private static LinkDto toDto(LinkLst e) {
        return new LinkDto(e.getLinkLstId(), e.getLinkUrl(), e.getCount());
    }

    private static EmailDto toEmailDto(PhMail e) {
        return new EmailDto(
                e.getPhMailId(),
                e.getPhFrom(),
                e.getRcpt(),
                e.getSub(),
                e.getRepTo(),
                e.getRetPath(),
                e.getTimestampMail());
    }
}
