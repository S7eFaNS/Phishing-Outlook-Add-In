package com.bank.phishaid.dashboard.dto;

import java.util.UUID;

public record ResultDto(
        UUID analysRsltId,
        UUID phMailId,
        Integer totalScore,
        String analysDesc,
        Boolean frwdToHost) {
}
