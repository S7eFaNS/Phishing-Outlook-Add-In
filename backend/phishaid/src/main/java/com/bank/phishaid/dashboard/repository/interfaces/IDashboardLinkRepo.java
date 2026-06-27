package com.bank.phishaid.dashboard.repository.interfaces;

import com.bank.phishaid.initialization.entity.LinkLst;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDashboardLinkRepo {

    Page<LinkLst> findAll(Pageable pageable);
}
