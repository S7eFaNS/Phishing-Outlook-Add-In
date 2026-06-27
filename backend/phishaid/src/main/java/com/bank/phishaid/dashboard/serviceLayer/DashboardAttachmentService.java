package com.bank.phishaid.dashboard.serviceLayer;

import com.bank.phishaid.dashboard.dto.AttachmentDto;
import com.bank.phishaid.dashboard.repository.interfaces.IDashboardAttachmentRepo;
import com.bank.phishaid.dashboard.serviceLayer.interfaces.IDashboardAttachmentService;
import com.bank.phishaid.initialization.entity.AttachmentLst;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class DashboardAttachmentService implements IDashboardAttachmentService {

    private final IDashboardAttachmentRepo repo;

    DashboardAttachmentService(IDashboardAttachmentRepo repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttachmentDto> getAll(Pageable pageable) {
        return repo.findAll(pageable).map(DashboardAttachmentService::toDto);
    }

    private static AttachmentDto toDto(AttachmentLst e) {
        return new AttachmentDto(e.getAttLstId(), e.getAttName(), e.getCount());
    }
}
