package com.bank.phishaid.initialization.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//temp cache table for retry logic on failure upon initialization

@Entity
@Table(name = "tempCache")
@Getter
@Setter
@NoArgsConstructor
public class TempCache {

    @Id
    @Column(name = "hashId")
    private String hashId;

    @Column(name = "rawEmail", columnDefinition = "TEXT")
    private String rawEmail;
}
