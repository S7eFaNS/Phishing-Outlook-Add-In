package com.bank.phishaid.dashboard.serviceLayer;

import com.bank.phishaid.analysis.entity.AnalysRslt;
import com.bank.phishaid.dashboard.dto.ResultDto;
import com.bank.phishaid.dashboard.repository.interfaces.IDashboardResultRepo;
import com.bank.phishaid.dashboard.serviceLayer.interfaces.IDashboardResultService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
class DashboardResultService implements IDashboardResultService {

    private final IDashboardResultRepo repo;

    DashboardResultService(IDashboardResultRepo repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResultDto> getAllResults(Pageable pageable) {
        return repo.findAll(pageable).map(DashboardResultService::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDto getResultById(UUID analysRsltId) {
        return repo.findById(analysRsltId)
                .map(DashboardResultService::toDto)
                .orElseThrow(() -> new NoSuchElementException("result " + analysRsltId + " not found"));
    }

    private static ResultDto toDto(AnalysRslt e) {
        return new ResultDto(
                e.getAnalysRsltId(),
                e.getPhMail().getPhMailId(),
                e.getTotalScore(),
                e.getAnalysDesc(),
                e.getFrwdToHost());
    }
}
