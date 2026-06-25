package com.bank.phishaid.initialization.dto;

import jakarta.validation.constraints.NotBlank;


public record ReceivePayload(
        @NotBlank String hash,
        @NotBlank String rawEmail) {
}
