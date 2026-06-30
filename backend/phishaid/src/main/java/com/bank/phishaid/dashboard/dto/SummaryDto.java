package com.bank.phishaid.dashboard.dto;

import com.bank.phishaid.analysis.model.Level;

import java.time.Instant;
import java.util.Map;

// how many (SAFE/LOW/MEDIUM/HIGH/CRITICAL) over a period of time

public record SummaryDto(
        long totalEmails,
        long totalResults,
        Map<Level, Long> levels,
        long frwdToHostCount,
        long incompleteCount,
        Instant from,
        Instant to) {
}
