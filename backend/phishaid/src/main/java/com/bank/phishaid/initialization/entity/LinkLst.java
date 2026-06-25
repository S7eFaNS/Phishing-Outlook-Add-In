package com.bank.phishaid.initialization.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

//List of urls
// unique id
// url links as text
// count for occurances in total

@Entity
@Table(name = "linkLst")
@Getter
@Setter
@NoArgsConstructor
public class LinkLst {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "linkLstId", updatable = false, nullable = false)
    private UUID linkLstId;

    // URLs (esp. tracking/redirect links) routinely exceed 255 chars. TEXT keeps the R1 UNIQUE;
    // the upsert's ON CONFLICT (linkUrl) still binds to that unique index.
    @Column(name = "linkUrl", columnDefinition = "TEXT", nullable = false, unique = true)
    private String linkUrl;

    @Column(name = "count")
    private Integer count;
}
