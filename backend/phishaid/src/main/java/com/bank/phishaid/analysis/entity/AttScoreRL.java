package com.bank.phishaid.analysis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

// Junction table between analysRslt and attScoreLst

@Entity
@Table(name = "attScoreRL")
@Getter
@Setter
@NoArgsConstructor
public class AttScoreRL {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "analysRsltId", nullable = false)
    private AnalysRslt analysRslt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attScoreId", nullable = false)
    private AttScoreLst attScoreLst;
}
