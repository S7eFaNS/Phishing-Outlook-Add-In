package com.bank.phishaid.dashboard.dto;

public record SenderCountDto(
        String senderDomain,
        long count) {
}
