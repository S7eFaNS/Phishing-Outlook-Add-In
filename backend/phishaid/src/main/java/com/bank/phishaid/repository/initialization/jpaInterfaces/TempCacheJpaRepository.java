package com.bank.phishaid.repository.initialization.jpaInterfaces;

import com.bank.phishaid.entity.TempCache;
import org.springframework.data.jpa.repository.JpaRepository;

//intentional public and no wrapper for the temp cache table

public interface TempCacheJpaRepository extends JpaRepository<TempCache, String> {
}
