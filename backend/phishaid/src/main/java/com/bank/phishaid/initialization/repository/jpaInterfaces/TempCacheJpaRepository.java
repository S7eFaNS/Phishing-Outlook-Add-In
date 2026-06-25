package com.bank.phishaid.initialization.repository.jpaInterfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.phishaid.initialization.entity.TempCache;

//intentional public and no wrapper for the temp cache table

public interface TempCacheJpaRepository extends JpaRepository<TempCache, String> {
}
