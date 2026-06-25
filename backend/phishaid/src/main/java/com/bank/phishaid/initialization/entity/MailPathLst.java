package com.bank.phishaid.initialization.entity;

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

// Authentication Header
// rcvMailPathId are the relay hops
// 1:1 FK with phMail
// senderIp is the first ip tracked
// SPF, DKIM, DMARC (true is pass, false is fail)

@Entity
@Table(name = "mailPathLst")
@Getter
@Setter
@NoArgsConstructor
public class MailPathLst {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "rcvMailPathId", updatable = false, nullable = false)
    private UUID rcvMailPathId;

    // unique FK 1:1 with phMail
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "phMailId", unique = true, nullable = false)
    private PhMail phMail;

    @Column(name = "senderIp", length = 255)
    private String senderIp;

    @Column(name = "mailSpf")
    private Boolean mailSpf;

    @Column(name = "mailDkim")
    private Boolean mailDkim;

    @Column(name = "mailDmarc")
    private Boolean mailDmarc;
}
