package com.bank.phishaid.initialization.serviceLayer;

import com.bank.phishaid.initialization.dto.BodyExtraction;
import com.bank.phishaid.initialization.dto.EmailDTO;
import com.bank.phishaid.initialization.entity.MailPathLst;
import com.bank.phishaid.initialization.entity.PhMail;
import com.bank.phishaid.analysis.serviceLayer.interfaces.IOrchestrationAnalysisService;
import com.bank.phishaid.initialization.serviceLayer.interfaces.IBodyInitService;
import com.bank.phishaid.initialization.serviceLayer.interfaces.IEmailParserService;
import com.bank.phishaid.initialization.serviceLayer.interfaces.IHeaderInitService;
import com.bank.phishaid.initialization.serviceLayer.interfaces.IOrchestrationInitService;
import com.bank.phishaid.initialization.serviceLayer.interfaces.ITempCacheService;

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
    private final IOrchestrationAnalysisService analysisOrchestration;

    OrchestrationInitService(ITempCacheService tempCache,
                             IEmailParserService parser,
                             IHeaderInitService headerInit,
                             IBodyInitService bodyInit,
                             IOrchestrationAnalysisService analysisOrchestration) {
        this.tempCache = tempCache;
        this.parser = parser;
        this.headerInit = headerInit;
        this.bodyInit = bodyInit;
        this.analysisOrchestration = analysisOrchestration;
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

        //Drop the checkpoint and hand off to analysis, but only after the transaction commits.
        registerPostCommitHook(hash, phMailId);

        log.info("Initialisation complete for phMail {} (checkpoint drop + analysis scheduled after commit)", phMailId);
    }

    //drop retry temp table if success and analysis takes over
    private void registerPostCommitHook(String hash, UUID phMailId) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    tempCache.Drop(hash);
                } catch (RuntimeException e) {
                    log.warn("Init committed but dropping tempCache checkpoint {} failed", hash, e);
                }
                try {
                    analysisOrchestration.ProcessAnalysis(phMailId);
                } catch (RuntimeException e) {
                    log.error("Analysis hand-off failed for phMail {} (init already committed)", phMailId, e);
                }
            }
        });
    }
}
