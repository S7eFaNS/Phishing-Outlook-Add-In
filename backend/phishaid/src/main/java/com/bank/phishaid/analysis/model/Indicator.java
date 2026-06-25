package com.bank.phishaid.analysis.model;

public enum Indicator {
    MALICIOUS_URL(3.0, Tier.STRONG),
    MALICIOUS_ATTACHMENT(3.0, Tier.STRONG),
    SPF_FAIL(0.8, Tier.SOFT),
    DKIM_FAIL(0.8, Tier.SOFT),
    DMARC_FAIL(0.8, Tier.SOFT),
    REPLY_TO_MISMATCH(0.6, Tier.SOFT),
    RETURN_PATH_MISMATCH(0.6, Tier.SOFT);

    public enum Tier { STRONG, SOFT }
    private final double weight; private final Tier tier;
    Indicator(double weight, Tier tier) { this.weight = weight; this.tier = tier; }
    public double weight() { return weight; }
    public Tier tier() { return tier; }
}
