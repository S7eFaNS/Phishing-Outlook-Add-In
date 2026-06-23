package com.bank.phishaid.serviceLayer.initialization;

import com.bank.phishaid.dto.EmailDTO;
import com.bank.phishaid.dto.EmailHeaderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EmailParserServiceRealEmailTest {

    private EmailParserService parserService;
    private String rawFixture;

    @BeforeEach
    void setUp() throws IOException {
        parserService = new EmailParserService();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("fixtures/Testing.eml")) {
            assertThat(in).as("fixture file must be on the test classpath").isNotNull();
            rawFixture = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Test
    void parsesHeaderFieldsFromTheRealMessage() {
        EmailHeaderDTO head = parserService.Parse(rawFixture).getHead();

        assertThat(head.getPhFrom()).isEqualTo("Stefan Galinov <stefannikola288@gmail.com>");
        assertThat(head.getRcpt()).isEqualTo("stefannikola298@gmail.com");
        assertThat(head.getSub()).isEqualTo("Testing");
        assertThat(head.getRepTo()).isNull(); // no Reply-To header present
        assertThat(head.getRetPath()).isEqualTo("<stefannikola288@gmail.com>");

        Instant expectedDate = ZonedDateTime.of(2026, 6, 9, 17, 27, 30, 0, ZoneOffset.ofHours(3)).toInstant();
        assertThat(head.getTimestampMail()).isEqualTo(expectedDate);
    }

    @Test
    void readsTheCanonicalAuthenticationResultsHeaderNotTheArcOne() {
        EmailHeaderDTO head = parserService.Parse(rawFixture).getHead();
        assertThat(head.getMailSpf()).isTrue();
        assertThat(head.getMailDkim()).isTrue();
        assertThat(head.getMailDmarc()).isTrue();
    }

    @Test
    void parsesBothGmailReceivedHopsAndDerivesSenderIpFromTheOldestOne() {
        EmailHeaderDTO head = parserService.Parse(rawFixture).getHead();
        assertThat(head.getRelayChain()).hasSize(2);
        assertThat(head.getRelayChain().get(0).getHopNumber()).isEqualTo(1);
        assertThat(head.getRelayChain().get(1).getHopNumber()).isEqualTo(2);
        assertThat(head.getRelayChain().get(1).getHopDescription()).contains("mail-sor-f41.google.com");

        assertThat(head.getSenderIp()).isEqualTo("209.85.220.41");
    }

    @Test
    void extractsThePlainTextBodyAndNoAttachments() {
        EmailDTO dto = parserService.Parse(rawFixture);

        assertThat(dto.getBody()).contains("test features");
        assertThat(dto.getAttachments()).isEmpty();
    }
}
