package com.bank.phishaid.dashboard.serviceLayer;

import com.bank.phishaid.analysis.entity.AnalysRslt;
import com.bank.phishaid.analysis.entity.AttScoreLst;
import com.bank.phishaid.analysis.entity.HeaderScoreLst;
import com.bank.phishaid.analysis.entity.LinkScoreLst;
import com.bank.phishaid.dashboard.dto.AttScoreDto;
import com.bank.phishaid.dashboard.dto.BreakdownDto;
import com.bank.phishaid.dashboard.dto.ResultDto;
import com.bank.phishaid.dashboard.dto.UrlScoreDto;
import com.bank.phishaid.dashboard.repository.interfaces.IDashboardResultRepo;
import com.bank.phishaid.dashboard.serviceLayer.interfaces.IDashboardResultService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Override
    @Transactional(readOnly = true)
    public ResultDto getResultByEmailId(UUID phMailId) {
        return repo.findByPhMailId(phMailId)
                .map(DashboardResultService::toDto)
                .orElseThrow(() -> new NoSuchElementException("result for email " + phMailId + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public BreakdownDto getBreakdown(UUID analysRsltId) {
        // 404 if the result is unknown; the breakdown fragments are read with separate queries.
        if (!repo.existsById(analysRsltId)) {
            throw new NoSuchElementException("result " + analysRsltId + " not found");
        }

        HeaderScoreLst header = repo.findHeaderScoreByResultId(analysRsltId).orElse(null);

        List<UrlScoreDto> urls = repo.findLinkScoresByResultId(analysRsltId).stream()
                .map(DashboardResultService::toUrlScoreDto)
                .toList();
        List<AttScoreDto> attachments = repo.findAttScoresByResultId(analysRsltId).stream()
                .map(DashboardResultService::toAttScoreDto)
                .toList();

        return new BreakdownDto(
                header == null ? null : header.getHeadScore(),
                header == null ? null : header.getSpfScore(),
                header == null ? null : header.getDkimScore(),
                header == null ? null : header.getDmarcScore(),
                header == null ? null : header.getReplyToMatch(),
                header == null ? null : header.getRetPathMatch(),
                urls,
                attachments);
    }

    private static ResultDto toDto(AnalysRslt e) {
        return new ResultDto(
                e.getAnalysRsltId(),
                e.getPhMail().getPhMailId(),
                e.getTotalScore(),
                e.getAnalysDesc(),
                e.getFrwdToHost());
    }

    private static UrlScoreDto toUrlScoreDto(LinkScoreLst e) {
        return new UrlScoreDto(e.getLinkLst().getLinkUrl(), e.getLinkScore());
    }

    private static AttScoreDto toAttScoreDto(AttScoreLst e) {
        return new AttScoreDto(e.getAttLst().getAttName(), e.getAttScore());
    }
}
