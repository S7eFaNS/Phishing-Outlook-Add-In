package com.bank.phishaid.dashboard.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record EmailDetailDto(
        UUID phMailId,
        String phFrom,
        String rcpt,
        String sub,
        String repTo,
        String retPath,
        Instant timestampMail,
        String senderIp,
        Boolean mailSpf,
        Boolean mailDkim,
        Boolean mailDmarc,
        List<RelayHopDto> relay,
        List<LinkDto> links,
        List<AttachmentDto> attachments,
        ResultDto result) {
}
