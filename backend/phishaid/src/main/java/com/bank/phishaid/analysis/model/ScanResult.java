package com.bank.phishaid.analysis.model;

import java.util.Set;

//VirusTotal API lookup (url, attachment)
//fired - indicators (url, attachment)
//complete - false (skipped = incomplete) 
public record ScanResult(Set<Indicator> fired, boolean complete) {
}
