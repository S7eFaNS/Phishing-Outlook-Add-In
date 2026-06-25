package com.bank.phishaid.initialization.serviceLayer;

import com.bank.phishaid.initialization.dto.BodyExtraction;
import com.bank.phishaid.initialization.dto.EmailAttachmentDTO;
import com.bank.phishaid.initialization.dto.EmailDTO;
import com.bank.phishaid.initialization.repository.interfaces.IAttachmentRepo;
import com.bank.phishaid.initialization.repository.interfaces.ILinkRepo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BodyInitServiceTest {

    // Independent known SHA-256 vectors — assert the impl against these, not against itself.
    private static final String SHA256_ABC =
            "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad";
    private static final String SHA256_EMPTY =
            "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

    @Mock private ILinkRepo linkRepo;
    @Mock private IAttachmentRepo attachmentRepo;

    @InjectMocks private BodyInitService service;

    @Test
    void extractsDistinctUrlsFromPlainAndHtmlAndStripsTrailingPunctuation() {
        EmailDTO dto = new EmailDTO();
        dto.setBody("Plain link http://evil.example/a, repeated http://evil.example/a. "
                + "Also <a href=\"https://safe.example/b\">click</a>");

        BodyExtraction result = service.ExtractUrlsAndAttachments(dto);

        // duplicate collapses to one; href form is captured without the closing quote
        assertThat(result.urls())
                .containsExactly("http://evil.example/a", "https://safe.example/b");
    }

    @Test
    void hashesAttachmentBytesWithSha256AndDeduplicates() {
        EmailDTO dto = new EmailDTO();
        dto.setBody("no urls here");
        dto.setAttachments(List.of(
                new EmailAttachmentDTO("a.bin", "abc".getBytes(StandardCharsets.UTF_8)),
                new EmailAttachmentDTO("b.bin", "abc".getBytes(StandardCharsets.UTF_8)), // same bytes
                new EmailAttachmentDTO("c.bin", new byte[0])));

        BodyExtraction result = service.ExtractUrlsAndAttachments(dto);

        assertThat(result.urls()).isEmpty();
        assertThat(result.attachmentHashes()).containsExactly(SHA256_ABC, SHA256_EMPTY);
    }

    @Test
    void handlesNullBodyAndNullAttachments() {
        BodyExtraction result = service.ExtractUrlsAndAttachments(new EmailDTO());

        assertThat(result.urls()).isEmpty();
        assertThat(result.attachmentHashes()).isEmpty();
    }

    @Test
    void initUrlUpsertsThenLinksEachUrlForThePhMail() {
        UUID phMailId = UUID.randomUUID();
        UUID idA = UUID.randomUUID();
        UUID idB = UUID.randomUUID();
        when(linkRepo.Create("http://evil.example/a")).thenReturn(idA);
        when(linkRepo.Create("https://safe.example/b")).thenReturn(idB);

        service.InitUrl(List.of("http://evil.example/a", "https://safe.example/b"), phMailId);

        // each URL: atomic upsert (Create) immediately followed by its junction insert
        var order = inOrder(linkRepo);
        order.verify(linkRepo).Create("http://evil.example/a");
        order.verify(linkRepo).CreateJunctionColumn(phMailId, idA);
        order.verify(linkRepo).Create("https://safe.example/b");
        order.verify(linkRepo).CreateJunctionColumn(phMailId, idB);
        verifyNoInteractions(attachmentRepo);
    }

    @Test
    void initAttachmentsUpsertsThenLinksEachHashForThePhMail() {
        UUID phMailId = UUID.randomUUID();
        UUID attId = UUID.randomUUID();
        when(attachmentRepo.Create(SHA256_ABC)).thenReturn(attId);

        service.InitAttachments(List.of(SHA256_ABC), phMailId);

        var order = inOrder(attachmentRepo);
        order.verify(attachmentRepo).Create(SHA256_ABC);
        order.verify(attachmentRepo).CreateJunctionColumn(phMailId, attId);
        verifyNoInteractions(linkRepo);
    }

    @Test
    void initUrlDoesNothingForEmptyList() {
        service.InitUrl(List.of(), UUID.randomUUID());

        verifyNoInteractions(linkRepo);
        verifyNoInteractions(attachmentRepo);
    }
}
