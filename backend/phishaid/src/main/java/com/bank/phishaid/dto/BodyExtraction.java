package com.bank.phishaid.dto;

import java.util.List;

// Result of IBodyInitService.ExtractUrlsAndAttachments

public record BodyExtraction(List<String> urls, List<String> attachmentHashes) {
}
