package com.bank.phishaid.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailHeaderDTO {
    private String phFrom;
    private String rcpt;
    private String sub;
    private String repTo;
    private String retPath;
    private Instant timestampMail;

    private String senderIp;
    private Boolean mailSpf;
    private Boolean mailDkim;
    private Boolean mailDmarc;

    private List<RelayHopDTO> relayChain;
}
