package com.bank.phishaid.initialization.serviceLayer;

import com.bank.phishaid.initialization.dto.BodyExtraction;
import com.bank.phishaid.initialization.dto.EmailAttachmentDTO;
import com.bank.phishaid.initialization.dto.EmailDTO;
import com.bank.phishaid.initialization.repository.interfaces.IAttachmentRepo;
import com.bank.phishaid.initialization.repository.interfaces.ILinkRepo;
import com.bank.phishaid.initialization.serviceLayer.interfaces.IBodyInitService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
class BodyInitService implements IBodyInitService {

    private static final Logger log = LoggerFactory.getLogger(BodyInitService.class);

    private static final Pattern URL_PATTERN =
            Pattern.compile("https?://[^\\s\"'<>]+", Pattern.CASE_INSENSITIVE);
    private static final Pattern TRAILING_PUNCT = Pattern.compile("[.,;:!?)\\]}>]+$");

    private final ILinkRepo linkRepo;
    private final IAttachmentRepo attachmentRepo;

    BodyInitService(ILinkRepo linkRepo, IAttachmentRepo attachmentRepo) {
        this.linkRepo = linkRepo;
        this.attachmentRepo = attachmentRepo;
    }

    //extracts urls and attachments if available and structures it in the EmailDTO for the initialization part
    @Override
    public BodyExtraction ExtractUrlsAndAttachments(EmailDTO dto) {
        List<String> urls = extractUrls(dto.getBody());
        List<String> attachmentHashes = hashAttachments(dto.getAttachments());
        log.info("Body extraction: {} url(s), {} attachment hash(es)", urls.size(), attachmentHashes.size());
        return new BodyExtraction(urls, attachmentHashes);
    }

    @Override
    public void InitUrl(List<String> urls, UUID phMailId) {
        for (String url : urls) {
            UUID linkLstId = linkRepo.Create(url);          
            linkRepo.CreateJunctionColumn(phMailId, linkLstId);
            log.debug("Linked url to phMail {} (linkLstId {})", phMailId, linkLstId);
        }
    }

    @Override
    public void InitAttachments(List<String> attachmentHashes, UUID phMailId) {
        for (String hash : attachmentHashes) {
            UUID attLstId = attachmentRepo.Create(hash);    
            attachmentRepo.CreateJunctionColumn(phMailId, attLstId);
            log.debug("Linked attachment hash to phMail {} (attLstId {})", phMailId, attLstId);
        }
    }

    //extract urls from body part of email
    private List<String> extractUrls(String body) {
        Set<String> distinct = new LinkedHashSet<>();
        if (body != null) {
            Matcher matcher = URL_PATTERN.matcher(body);
            while (matcher.find()) {
                String url = TRAILING_PUNCT.matcher(matcher.group()).replaceAll("");
                if (!url.isBlank()) {
                    distinct.add(url);
                }
            }
        }
        return new ArrayList<>(distinct);
    }

    //hash attachments when retrieving them from email body
    //attachment hashes are used to check the attachment in virustotal, VT has attachment hash checkup

    private List<String> hashAttachments(List<EmailAttachmentDTO> attachments) {
        Set<String> distinct = new LinkedHashSet<>();
        if (attachments != null) {
            for (EmailAttachmentDTO attachment : attachments) {
                if (attachment != null && attachment.getContent() != null) {
                    distinct.add(sha256Hex(attachment.getContent()));
                }
            }
        }
        return new ArrayList<>(distinct);
    }

    //sha256 encoder of the attachments
    private static String sha256Hex(byte[] data) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(data);
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}
