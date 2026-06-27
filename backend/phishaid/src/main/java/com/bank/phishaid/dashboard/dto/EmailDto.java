package com.bank.phishaid.dashboard.dto;

import java.time.Instant;
import java.util.UUID;

public record EmailDto(
        UUID phMailId,
        String phFrom,
        String rcpt,
        String sub,
        String repTo,
        String retPath,
        Instant timestampMail) {
}
