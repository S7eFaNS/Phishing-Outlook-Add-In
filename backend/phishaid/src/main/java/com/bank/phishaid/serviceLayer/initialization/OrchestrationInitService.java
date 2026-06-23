package com.bank.phishaid.serviceLayer.initialization;

import com.bank.phishaid.dto.BodyExtraction;
import com.bank.phishaid.dto.EmailDTO;
import com.bank.phishaid.entity.MailPathLst;
import com.bank.phishaid.entity.PhMail;
import com.bank.phishaid.serviceLayer.initialization.interfaces.IBodyInitService;
import com.bank.phishaid.serviceLayer.initialization.interfaces.IEmailParserService;
import com.bank.phishaid.serviceLayer.initialization.interfaces.IHeaderInitService;
import com.bank.phishaid.serviceLayer.initialization.interfaces.IOrchestrationInitService;
import com.bank.phishaid.serviceLayer.initialization.interfaces.ITempCacheService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.UUID;

@Service
class OrchestrationInitService implements IOrchestrationInitService {

    private static final Logger log = LoggerFactory.getLogger(OrchestrationInitService.class);

    private final ITempCacheService tempCache;
    private final IEmailParserService parser;
    private final IHeaderInitService headerInit;
    private final IBodyInitService bodyInit;

    OrchestrationInitService(ITempCacheService tempCache,
                             IEmailParserService parser,
                             IHeaderInitService headerInit,
                             IBodyInitService bodyInit) {
        this.tempCache = tempCache;
        this.parser = parser;
        this.headerInit = headerInit;
        this.bodyInit = bodyInit;
    }

    @Override
    @Transactional
    public void ProcessInitializationMail(String hash, String rawEmail) {

        //saves hash and rawEmail to temp table for retry logic
        tempCache.Save(hash, rawEmail);

        //Parse raw .eml to structured DTO
        EmailDTO dto = parser.Parse(rawEmail);

        //Header initialization
        PhMail phMail = headerInit.InitPhMail(dto.getHead());
        MailPathLst mailPath = headerInit.InitMailPath(dto.getHead(), phMail);
        headerInit.InitMailRelay(dto.getHead(), mailPath);

        //Body initialization
        UUID phMailId = phMail.getPhMailId();
        BodyExtraction extraction = bodyInit.ExtractUrlsAndAttachments(dto);
        bodyInit.InitUrl(extraction.urls(), phMailId);
        bodyInit.InitAttachments(extraction.attachmentHashes(), phMailId);

        //Drop the checkpoint, but only after the transaction commits.
        registerCheckpointDrop(hash);

        log.info("Initialisation complete for phMail {} (checkpoint drop scheduled after commit)", phMailId);

        // Analysis hand-off (IOrchestrationAnalysisService) is a under work — not called.
    }

    private void registerCheckpointDrop(String hash) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    tempCache.Drop(hash);
                } catch (RuntimeException e) {
                    log.warn("Init committed but dropping tempCache checkpoint {} failed", hash, e);
                }
            }
        });
    }
}
