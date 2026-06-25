package com.bank.phishaid.analysis.entity;

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

// One row per analysed URL occurrence

@Entity
@Table(name = "linkScoreLst")
@Getter
@Setter
@NoArgsConstructor
public class LinkScoreLst {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "linkScoreId", updatable = false, nullable = false)
    private UUID linkScoreId;

    @Column(name = "linkScore")
    private Integer linkScore;
}
