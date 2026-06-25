package com.bank.phishaid.analysis.model;

public enum Level {
    SAFE(0,20), LOW(20,40), MEDIUM(40,60), HIGH(60,80), CRITICAL(80,101);
    private final int min, max;
    Level(int min, int max) { this.min = min; this.max = max; }
    public static Level of(int score) {
        for (Level l : values()) if (score >= l.min && score < l.max) return l;
        return CRITICAL; // unreachable for valid 0–100
    }
}
