package com.bank.phishaid.dashboard.serviceLayer.interfaces;

import com.bank.phishaid.dashboard.dto.LinkDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDashboardLinkService {

    Page<LinkDto> getAll(Pageable pageable);
}
