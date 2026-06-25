package com.bank.phishaid.initialization.entity;

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

// Junction table with phMailId and attLstId

@Entity
@Table(name = "attachmentRL")
@Getter
@Setter
@NoArgsConstructor
public class AttachmentRL {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "phMailId", nullable = false)
    private PhMail phMail;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attLstId", nullable = false)
    private AttachmentLst attLst;
}
