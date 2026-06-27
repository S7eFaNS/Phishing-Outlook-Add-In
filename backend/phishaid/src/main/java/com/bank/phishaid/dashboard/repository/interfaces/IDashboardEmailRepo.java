package com.bank.phishaid.dashboard.repository.interfaces;

import com.bank.phishaid.initialization.entity.PhMail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface IDashboardEmailRepo {

    Page<PhMail> findAll(Pageable pageable);

    Optional<PhMail> findById(UUID phMailId);
}
