package com.bank.phishaid.dashboard.dto;

import java.util.UUID;

public record LinkDto(
        UUID linkLstId,
        String linkUrl,
        Integer count) {
}
