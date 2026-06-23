package com.bank.phishaid.repository.initialization.jpaInterfaces;

import com.bank.phishaid.entity.LinkRL;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LinkRLJpaRepository extends JpaRepository<LinkRL, UUID> {
}
