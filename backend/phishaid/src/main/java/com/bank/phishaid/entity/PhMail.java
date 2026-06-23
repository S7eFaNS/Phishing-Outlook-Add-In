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

import java.time.Instant;
import java.util.UUID;

//Main email information
//From, Rcpt, Sub, Reply-to, Return-Path, Timestamp of email

@Entity
@Table(name = "phMail")
@Getter
@Setter
@NoArgsConstructor
public class PhMail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "phMailId", updatable = false, nullable = false)
    private UUID phMailId;

    @Column(name = "phFrom", columnDefinition = "TEXT")
    private String phFrom;

    @Column(name = "rcpt", columnDefinition = "TEXT")
    private String rcpt;

    @Column(name = "sub", columnDefinition = "TEXT")
    private String sub;

    @Column(name = "repTo", columnDefinition = "TEXT")
    private String repTo;

    @Column(name = "retPath", columnDefinition = "TEXT")
    private String retPath;

    @Column(name = "timestampMail")
    private Instant timestampMail;
}

