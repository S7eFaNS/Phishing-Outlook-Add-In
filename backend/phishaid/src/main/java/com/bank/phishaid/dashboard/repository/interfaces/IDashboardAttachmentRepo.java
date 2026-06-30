package com.bank.phishaid.dashboard.repository.interfaces;

import com.bank.phishaid.initialization.entity.AttachmentLst;
import com.bank.phishaid.initialization.entity.PhMail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IDashboardAttachmentRepo {

    Page<AttachmentLst> findAll(Pageable pageable);

    boolean existsById(UUID attLstId);

    Page<PhMail> findEmailsForAttachment(UUID attLstId, Pageable pageable);
}
