package com.bank.phishaid.dashboard.dto;

import java.util.UUID;

public record AttachmentDto(
        UUID attLstId,
        String attName,
        Integer count) {
}
