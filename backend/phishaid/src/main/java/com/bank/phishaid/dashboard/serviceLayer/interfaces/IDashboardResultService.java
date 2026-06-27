package com.bank.phishaid.dashboard.serviceLayer.interfaces;

import com.bank.phishaid.dashboard.dto.ResultDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IDashboardResultService {

    Page<ResultDto> getAllResults(Pageable pageable);

    ResultDto getResultById(UUID analysRsltId);
}
