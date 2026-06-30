package com.bank.phishaid.dashboard.dto;

public record UrlScoreDto(
        String linkUrl,
        Integer linkScore) {
}
