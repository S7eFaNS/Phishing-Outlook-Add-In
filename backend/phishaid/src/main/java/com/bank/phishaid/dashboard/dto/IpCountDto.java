package com.bank.phishaid.dashboard.dto;

public record IpCountDto(
        String senderIp,
        long count) {
}
