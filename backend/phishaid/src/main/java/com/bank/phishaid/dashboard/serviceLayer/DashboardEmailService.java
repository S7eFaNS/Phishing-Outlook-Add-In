package com.bank.phishaid.dashboard.serviceLayer;

import com.bank.phishaid.dashboard.dto.EmailDto;
import com.bank.phishaid.dashboard.repository.interfaces.IDashboardEmailRepo;
import com.bank.phishaid.dashboard.serviceLayer.interfaces.IDashboardEmailService;
import com.bank.phishaid.initialization.entity.PhMail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
class DashboardEmailService implements IDashboardEmailService {

    private final IDashboardEmailRepo repo;

    DashboardEmailService(IDashboardEmailRepo repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmailDto> getAllEmails(Pageable pageable) {
        return repo.findAll(pageable).map(DashboardEmailService::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public EmailDto getEmailById(UUID phMailId) {
        return repo.findById(phMailId)
                .map(DashboardEmailService::toDto)
                .orElseThrow(() -> new NoSuchElementException("email " + phMailId + " not found"));
    }

    private static EmailDto toDto(PhMail e) {
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
