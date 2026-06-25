package com.bank.phishaid.analysis.serviceLayer;

import com.bank.phishaid.analysis.model.Indicator;
import com.bank.phishaid.analysis.model.Level;
import com.bank.phishaid.analysis.model.ScoreResult;

import java.util.Set;

public class ThreatScorer {
    private static final double MIDPOINT = 2.5;
    private static final int SOFT_CEILING = 59;
    public ScoreResult score(Set<Indicator> fired) {
        double evidence = 0.0; boolean hasStrong = false;
        for (Indicator i : fired) {
            evidence += i.weight();
            if (i.tier() == Indicator.Tier.STRONG) hasStrong = true;
        }
        double raw = 100.0 / (1.0 + Math.exp(-(evidence - MIDPOINT)));
        double guarded = hasStrong ? raw : Math.min(raw, SOFT_CEILING);
        int finalScore = (int) Math.round(guarded);
        return new ScoreResult(finalScore, Level.of(finalScore));
    }
}
