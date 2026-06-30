package com.bank.phishaid.dashboard.repository.interfaces;

import com.bank.phishaid.initialization.entity.LinkLst;
import com.bank.phishaid.initialization.entity.PhMail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IDashboardLinkRepo {

    Page<LinkLst> findAll(Pageable pageable);

    boolean existsById(UUID linkLstId);

    Page<PhMail> findEmailsForLink(UUID linkLstId, Pageable pageable);
}
