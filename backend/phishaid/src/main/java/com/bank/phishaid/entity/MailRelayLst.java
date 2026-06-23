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

// Each relay hop in a email

@Entity
@Table(name = "mailRelayLst")
@Getter
@Setter
@NoArgsConstructor
public class MailRelayLst {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "mailRelayId", updatable = false, nullable = false)
    private UUID mailRelayId;

    @Column(name = "hopNumber")
    private Integer hopNumber;

    @Column(name = "hopDescription", columnDefinition = "TEXT")
    private String hopDescription;
}
