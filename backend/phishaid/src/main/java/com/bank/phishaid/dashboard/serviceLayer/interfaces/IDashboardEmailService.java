package com.bank.phishaid.dashboard.serviceLayer.interfaces;

import com.bank.phishaid.dashboard.dto.EmailDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IDashboardEmailService {

    Page<EmailDto> getAllEmails(Pageable pageable);

    EmailDto getEmailById(UUID phMailId);
}
