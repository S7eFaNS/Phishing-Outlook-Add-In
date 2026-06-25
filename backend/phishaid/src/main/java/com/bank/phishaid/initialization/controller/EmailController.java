package com.bank.phishaid.initialization.controller;

import com.bank.phishaid.initialization.dto.ReceivePayload;
import com.bank.phishaid.initialization.serviceLayer.interfaces.IOrchestrationInitService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
class EmailController {

    private final IOrchestrationInitService orchestration;

    EmailController(IOrchestrationInitService orchestration) {
        this.orchestration = orchestration;
    }

    //entry point for payload, @Valid if empty payload = 400, 500 on unexpected failures

    @PostMapping("/receive")
    public ResponseEntity<Void> GetReceiveRawData(@Valid @RequestBody ReceivePayload payload) {
        orchestration.ProcessInitializationMail(payload.hash(), payload.rawEmail());
        return ResponseEntity.ok().build();
    }
}
