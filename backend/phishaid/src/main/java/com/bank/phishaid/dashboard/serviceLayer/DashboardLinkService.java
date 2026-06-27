package com.bank.phishaid.dashboard.serviceLayer;

import com.bank.phishaid.dashboard.dto.LinkDto;
import com.bank.phishaid.dashboard.repository.interfaces.IDashboardLinkRepo;
import com.bank.phishaid.dashboard.serviceLayer.interfaces.IDashboardLinkService;
import com.bank.phishaid.initialization.entity.LinkLst;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private static LinkDto toDto(LinkLst e) {
        return new LinkDto(e.getLinkLstId(), e.getLinkUrl(), e.getCount());
    }
}
