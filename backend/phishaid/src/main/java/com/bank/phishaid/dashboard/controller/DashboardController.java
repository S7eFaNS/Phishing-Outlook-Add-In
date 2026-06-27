package com.bank.phishaid.dashboard.controller;

import com.bank.phishaid.dashboard.dto.AttachmentDto;
import com.bank.phishaid.dashboard.dto.EmailDto;
import com.bank.phishaid.dashboard.dto.LinkDto;
import com.bank.phishaid.dashboard.dto.ResultDto;
import com.bank.phishaid.dashboard.serviceLayer.interfaces.IDashboardAttachmentService;
import com.bank.phishaid.dashboard.serviceLayer.interfaces.IDashboardEmailService;
import com.bank.phishaid.dashboard.serviceLayer.interfaces.IDashboardLinkService;
import com.bank.phishaid.dashboard.serviceLayer.interfaces.IDashboardResultService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/api/dashboard")
class DashboardController {

    private final IDashboardEmailService emailService;
    private final IDashboardLinkService linkService;
    private final IDashboardAttachmentService attachmentService;
    private final IDashboardResultService resultService;

    DashboardController(IDashboardEmailService emailService,
                        IDashboardLinkService linkService,
                        IDashboardAttachmentService attachmentService,
                        IDashboardResultService resultService) {
        this.emailService = emailService;
        this.linkService = linkService;
        this.attachmentService = attachmentService;
        this.resultService = resultService;
    }

    @GetMapping("/emails")
    public Page<EmailDto> getEmails(@PageableDefault(size = 20) Pageable pageable) {
        return emailService.getAllEmails(pageable);
    }

    @GetMapping("/emails/{id}")
    public EmailDto getEmail(@PathVariable UUID id) {
        return emailService.getEmailById(id);
    }

    @GetMapping("/links")
    public Page<LinkDto> getLinks(@PageableDefault(size = 20) Pageable pageable) {
        return linkService.getAll(pageable);
    }

    @GetMapping("/attachments")
    public Page<AttachmentDto> getAttachments(@PageableDefault(size = 20) Pageable pageable) {
        return attachmentService.getAll(pageable);
    }

    @GetMapping("/results")
    public Page<ResultDto> getResults(@PageableDefault(size = 20) Pageable pageable) {
        return resultService.getAllResults(pageable);
    }

    @GetMapping("/results/{id}")
    public ResultDto getResult(@PathVariable UUID id) {
        return resultService.getResultById(id);
    }
}
