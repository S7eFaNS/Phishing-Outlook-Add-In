package com.bank.phishaid.dto;

import jakarta.validation.constraints.NotBlank;


public record ReceivePayload(
        @NotBlank String hash,
        @NotBlank String rawEmail) {
}
