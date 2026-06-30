package com.bank.phishaid.dashboard.serviceLayer.interfaces;

import com.bank.phishaid.dashboard.dto.EmailDto;
import com.bank.phishaid.dashboard.dto.LinkDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IDashboardLinkService {

    Page<LinkDto> getAll(Pageable pageable);

    Page<EmailDto> getEmailsForLink(UUID linkLstId, Pageable pageable);
}
