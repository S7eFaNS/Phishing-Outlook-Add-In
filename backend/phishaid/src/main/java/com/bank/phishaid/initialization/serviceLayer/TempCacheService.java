package com.bank.phishaid.initialization.serviceLayer;

import com.bank.phishaid.initialization.entity.TempCache;
import com.bank.phishaid.initialization.repository.jpaInterfaces.TempCacheJpaRepository;
import com.bank.phishaid.initialization.serviceLayer.interfaces.IOrchestrationInitService;
import com.bank.phishaid.initialization.serviceLayer.interfaces.ITempCacheService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
class TempCacheService implements ITempCacheService {

    private static final Logger log = LoggerFactory.getLogger(TempCacheService.class);

    private final TempCacheJpaRepository repo;

    private final IOrchestrationInitService orchestration;

    TempCacheService(TempCacheJpaRepository repo, @Lazy IOrchestrationInitService orchestration) {
        this.repo = repo;
        this.orchestration = orchestration;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void Save(String hash, String rawMail) {
        TempCache checkpoint = new TempCache();
        checkpoint.setHashId(hash);
        checkpoint.setRawEmail(rawMail);
        repo.save(checkpoint);
        log.info("Saved tempCache checkpoint for hash {}", hash);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void Drop(String hash) {
        repo.findById(hash).ifPresent(repo::delete);
        log.info("Dropped tempCache checkpoint for hash {}", hash);
    }

    //retry logic if initialization fails from temp table
    @Override
    public void Retry(String hash) {
        TempCache checkpoint = repo.findById(hash).orElseThrow(
                () -> new IllegalArgumentException("No tempCache checkpoint for hash " + hash));
        log.info("Retrying initialization from tempCache checkpoint {}", hash);
        orchestration.ProcessInitializationMail(checkpoint.getHashId(), checkpoint.getRawEmail());
    }
}
