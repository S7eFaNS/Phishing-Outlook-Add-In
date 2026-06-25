package com.bank.phishaid.initialization.serviceLayer;

import com.bank.phishaid.initialization.dto.EmailDTO;
import com.bank.phishaid.initialization.dto.EmailHeaderDTO;
import com.bank.phishaid.initialization.dto.RelayHopDTO;
import com.bank.phishaid.exception.EmailParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailParserServiceTest {

    private EmailParserService parserService;
    private String rawFixture;

    @BeforeEach
    void setUp() throws IOException {
        parserService = new EmailParserService();
        try (InputStream in = getClass().getClassLoader()
                .getResourceAsStream("fixtures/sample-phishing.eml")) {
            assertThat(in).as("fixture file must be on the test classpath").isNotNull();
            rawFixture = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Test
    void parsesHeaderFieldsOntoTheHeadSection() {
        EmailDTO dto = parserService.Parse(rawFixture);
        EmailHeaderDTO head = dto.getHead();

        assertThat(head.getPhFrom()).isEqualTo("\"PayPal Service\" <service@paypal-secure-verify.com>");
        assertThat(head.getRcpt()).isEqualTo("victim@unicreditbulbank.bg");
        assertThat(head.getSub()).isEqualTo("Urgent: Verify Your Account");
        assertThat(head.getRepTo()).isEqualTo("<noreply@paypal-secure-verify.com>");
        assertThat(head.getRetPath()).isEqualTo("<bounce@paypal-secure-verify.com>");

        Instant expectedDate = ZonedDateTime.of(2025, 6, 16, 8, 15, 23, 0, ZoneOffset.UTC).toInstant();
        assertThat(head.getTimestampMail()).isEqualTo(expectedDate);
    }

    @Test
    void parsesAuthenticationResultsAsBooleans() {
        EmailHeaderDTO head = parserService.Parse(rawFixture).getHead();

        assertThat(head.getMailSpf()).isFalse();
        assertThat(head.getMailDkim()).isFalse();
        assertThat(head.getMailDmarc()).isFalse();
    }

    @Test
    void parsesRelayChainInOrderAndDerivesSenderIpFromOldestHop() {
        EmailHeaderDTO head = parserService.Parse(rawFixture).getHead();

        assertThat(head.getRelayChain()).hasSize(3);

        RelayHopDTO hop1 = head.getRelayChain().get(0);
        assertThat(hop1.getHopNumber()).isEqualTo(1);
        assertThat(hop1.getHopDescription()).contains("mx.unicreditbulbank.bg");

        RelayHopDTO hop3 = head.getRelayChain().get(2);
        assertThat(hop3.getHopNumber()).isEqualTo(3);
        assertThat(hop3.getHopDescription()).contains("attacker-host.example");

        // sender IP is read from the oldest (last) hop, closest to the true origin
        assertThat(head.getSenderIp()).isEqualTo("198.51.100.23");
    }

    @Test
    void extractsBodyTextFromBothPlainAndHtmlParts() {
        EmailDTO dto = parserService.Parse(rawFixture);

        assertThat(dto.getBody())
                .contains("Please verify your account at http://paypal-secure-verify.com/login")
                .contains("href=\"http://paypal-secure-verify.com/login\"");
    }

    @Test
    void extractsInnerAttachments() {
        EmailDTO dto = parserService.Parse(rawFixture);

        assertThat(dto.getAttachments()).hasSize(1);
        assertThat(dto.getAttachments().get(0).getFileName()).isEqualTo("invoice.pdf");
        assertThat(dto.getAttachments().get(0).getContent())
                .isEqualTo("FAKE-PDF-CONTENT-FOR-UNIT-TEST".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void rejectsBlankInput() {
        assertThatThrownBy(() -> parserService.Parse("  "))
                .isInstanceOf(EmailParseException.class);
    }
}
