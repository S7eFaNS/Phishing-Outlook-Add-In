package com.bank.phishaid.dashboard.dto;

import java.util.List;

public record BreakdownDto(
        Integer headScore,
        String spfScore,
        String dkimScore,
        String dmarcScore,
        Boolean replyToMatch,
        Boolean retPathMatch,
        List<UrlScoreDto> urls,
        List<AttScoreDto> attachments) {
}
