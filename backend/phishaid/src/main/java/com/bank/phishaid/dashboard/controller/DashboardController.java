package com.bank.phishaid.dashboard.controller;

import com.bank.phishaid.dashboard.dto.AttachmentDto;
import com.bank.phishaid.dashboard.dto.BreakdownDto;
import com.bank.phishaid.dashboard.dto.EmailDetailDto;
import com.bank.phishaid.dashboard.dto.EmailDto;
import com.bank.phishaid.dashboard.dto.IpCountDto;
import com.bank.phishaid.dashboard.dto.LinkDto;
import com.bank.phishaid.dashboard.dto.ResultDto;
import com.bank.phishaid.dashboard.dto.SenderCountDto;
import com.bank.phishaid.dashboard.dto.SummaryDto;
import com.bank.phishaid.dashboard.serviceLayer.interfaces.IDashboardAttachmentService;
import com.bank.phishaid.dashboard.serviceLayer.interfaces.IDashboardEmailService;
import com.bank.phishaid.dashboard.serviceLayer.interfaces.IDashboardLinkService;
import com.bank.phishaid.dashboard.serviceLayer.interfaces.IDashboardResultService;
import com.bank.phishaid.dashboard.serviceLayer.interfaces.IDashboardStatsService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/dashboard")
class DashboardController {

    private final IDashboardEmailService emailService;
    private final IDashboardLinkService linkService;
    private final IDashboardAttachmentService attachmentService;
    private final IDashboardResultService resultService;
    private final IDashboardStatsService statsService;

    DashboardController(IDashboardEmailService emailService,
                        IDashboardLinkService linkService,
                        IDashboardAttachmentService attachmentService,
                        IDashboardResultService resultService,
                        IDashboardStatsService statsService) {
        this.emailService = emailService;
        this.linkService = linkService;
        this.attachmentService = attachmentService;
        this.resultService = resultService;
        this.statsService = statsService;
    }

    @GetMapping("/emails")
    public Page<EmailDto> getEmails(@PageableDefault(size = 20) Pageable pageable) {
        return emailService.getAllEmails(pageable);
    }

    @GetMapping("/emails/{id}")
    public EmailDto getEmail(@PathVariable UUID id) {
        return emailService.getEmailById(id);
    }

    @GetMapping("/emails/{id}/detail")
    public EmailDetailDto getEmailDetail(@PathVariable UUID id) {
        return emailService.getEmailDetail(id);
    }

    @GetMapping("/emails/{id}/result")
    public ResultDto getEmailResult(@PathVariable UUID id) {
        return resultService.getResultByEmailId(id);
    }

    @GetMapping("/links")
    public Page<LinkDto> getLinks(@PageableDefault(size = 20) Pageable pageable) {
        return linkService.getAll(pageable);
    }

    @GetMapping("/links/{id}/emails")
    public Page<EmailDto> getEmailsForLink(@PathVariable UUID id,
                                           @PageableDefault(size = 20) Pageable pageable) {
        return linkService.getEmailsForLink(id, pageable);
    }

    @GetMapping("/attachments")
    public Page<AttachmentDto> getAttachments(@PageableDefault(size = 20) Pageable pageable) {
        return attachmentService.getAll(pageable);
    }

    @GetMapping("/attachments/{id}/emails")
    public Page<EmailDto> getEmailsForAttachment(@PathVariable UUID id,
                                                 @PageableDefault(size = 20) Pageable pageable) {
        return attachmentService.getEmailsForAttachment(id, pageable);
    }

    @GetMapping("/results")
    public Page<ResultDto> getResults(@PageableDefault(size = 20) Pageable pageable) {
        return resultService.getAllResults(pageable);
    }

    @GetMapping("/results/{id}")
    public ResultDto getResult(@PathVariable UUID id) {
        return resultService.getResultById(id);
    }

    @GetMapping("/results/{id}/breakdown")
    public BreakdownDto getBreakdown(@PathVariable UUID id) {
        return resultService.getBreakdown(id);
    }

    @GetMapping("/stats/top-senders")
    public List<SenderCountDto> getTopSenders(@RequestParam(defaultValue = "10") int limit) {
        return statsService.getTopSenders(limit);
    }

    @GetMapping("/stats/top-sender-ips")
    public List<IpCountDto> getTopSenderIps(@RequestParam(defaultValue = "10") int limit) {
        return statsService.getTopSenderIps(limit);
    }

    @GetMapping("/stats/summary")
    public SummaryDto getSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        // Default window: the last 30 days.
        Instant resolvedTo = (to != null) ? to : Instant.now();
        Instant resolvedFrom = (from != null) ? from : resolvedTo.minus(30, ChronoUnit.DAYS);
        return statsService.getSummary(resolvedFrom, resolvedTo);
    }
}
