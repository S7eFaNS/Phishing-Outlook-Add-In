package com.bank.phishaid.dashboard.serviceLayer;

import com.bank.phishaid.dashboard.dto.AttachmentDto;
import com.bank.phishaid.dashboard.dto.EmailDto;
import com.bank.phishaid.dashboard.repository.interfaces.IDashboardAttachmentRepo;
import com.bank.phishaid.dashboard.serviceLayer.interfaces.IDashboardAttachmentService;
import com.bank.phishaid.initialization.entity.AttachmentLst;
import com.bank.phishaid.initialization.entity.PhMail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

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

    @Override
    @Transactional(readOnly = true)
    public Page<EmailDto> getEmailsForAttachment(UUID attLstId, Pageable pageable) {
        if (!repo.existsById(attLstId)) {
            throw new NoSuchElementException("attachment " + attLstId + " not found");
        }
        return repo.findEmailsForAttachment(attLstId, pageable).map(DashboardAttachmentService::toEmailDto);
    }

    private static AttachmentDto toDto(AttachmentLst e) {
        return new AttachmentDto(e.getAttLstId(), e.getAttName(), e.getCount());
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
