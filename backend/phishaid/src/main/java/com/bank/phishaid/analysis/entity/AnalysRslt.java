package com.bank.phishaid.analysis.entity;

import com.bank.phishaid.initialization.entity.PhMail;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

// One analysis result per analysed email
// 1:1 FK with phMail
// totalScore is 0-100 from ThreatScorer over the union of fired indicators
// frwdToHost is true if a header auth check failed (notify-eligible condition, nothing dispatched currently)

@Entity
@Table(name = "analysRslt")
@Getter
@Setter
@NoArgsConstructor
public class AnalysRslt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "analysRsltId", updatable = false, nullable = false)
    private UUID analysRsltId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "phMailId", unique = true, nullable = false)
    private PhMail phMail;

    @Column(name = "totalScore")
    private Integer totalScore;

    @Column(name = "analysDesc", length = 255)
    private String analysDesc;

    @Column(name = "frwdToHost")
    private Boolean frwdToHost;
}
