package com.bank.phishaid.analysis.dto;

public record Verdict(boolean malicious, int maliciousCount, int suspiciousCount, boolean available) {

    public static Verdict unavailable() {
        return new Verdict(false, 0, 0, false);
    }
}
