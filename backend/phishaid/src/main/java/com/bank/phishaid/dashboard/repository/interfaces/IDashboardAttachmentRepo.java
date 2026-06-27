package com.bank.phishaid.dashboard.repository.interfaces;

import com.bank.phishaid.initialization.entity.AttachmentLst;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDashboardAttachmentRepo {

    Page<AttachmentLst> findAll(Pageable pageable);
}
