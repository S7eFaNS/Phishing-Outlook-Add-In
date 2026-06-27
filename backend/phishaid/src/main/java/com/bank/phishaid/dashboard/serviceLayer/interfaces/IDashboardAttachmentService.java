package com.bank.phishaid.dashboard.serviceLayer.interfaces;

import com.bank.phishaid.dashboard.dto.AttachmentDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDashboardAttachmentService {

    Page<AttachmentDto> getAll(Pageable pageable);
}
