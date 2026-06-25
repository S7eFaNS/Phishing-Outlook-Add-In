package com.bank.phishaid.analysis.entity;

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

// 1:1 with analysRslt via a direct FK
// spf/dkim/dmarcScore are tokens derived from mailPathLst's booleans ("pass"/"fail")
// replyToMatch / retPathMatch are true when the From-domain matches (or the header is absent)

@Entity
@Table(name = "headerScoreLst")
@Getter
@Setter
@NoArgsConstructor
public class HeaderScoreLst {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "analysRsltId", unique = true, nullable = false)
    private AnalysRslt analysRslt;

    @Column(name = "headScore")
    private Integer headScore;

    @Column(name = "spfScore", length = 16)
    private String spfScore;

    @Column(name = "dkimScore", length = 16)
    private String dkimScore;

    @Column(name = "dmarcScore", length = 16)
    private String dmarcScore;

    @Column(name = "replyToMatch")
    private Boolean replyToMatch;

    @Column(name = "retPathMatch")
    private Boolean retPathMatch;
}
