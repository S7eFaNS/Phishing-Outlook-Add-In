package com.bank.phishaid.entity;

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


// Master list of attachments
// unique ID
// Name is the attachment hash bytes
// count is number of times seen

@Entity
@Table(name = "attachmentLst")
@Getter
@Setter
@NoArgsConstructor
public class AttachmentLst {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "attLstId", updatable = false, nullable = false)
    private UUID attLstId;

    @Column(name = "attName", length = 255, nullable = false, unique = true)
    private String attName;

    @Column(name = "count")
    private Integer count;
}
