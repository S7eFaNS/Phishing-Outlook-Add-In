package com.bank.phishaid.dashboard.serviceLayer.interfaces;

import com.bank.phishaid.dashboard.dto.AttachmentDto;
import com.bank.phishaid.dashboard.dto.EmailDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IDashboardAttachmentService {

    Page<AttachmentDto> getAll(Pageable pageable);

    Page<EmailDto> getEmailsForAttachment(UUID attLstId, Pageable pageable);
}
