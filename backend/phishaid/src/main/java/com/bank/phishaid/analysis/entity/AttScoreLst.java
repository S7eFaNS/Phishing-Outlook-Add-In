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

// One row per analysed attachment occurrence

@Entity
@Table(name = "attScoreLst")
@Getter
@Setter
@NoArgsConstructor
public class AttScoreLst {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "attScoreId", updatable = false, nullable = false)
    private UUID attScoreId;

    @Column(name = "attScore")
    private Integer attScore;
}
