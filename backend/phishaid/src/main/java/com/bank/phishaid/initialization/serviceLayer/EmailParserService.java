package com.bank.phishaid.initialization.serviceLayer;

import com.bank.phishaid.initialization.dto.EmailAttachmentDTO;
import com.bank.phishaid.initialization.dto.EmailDTO;
import com.bank.phishaid.initialization.dto.EmailHeaderDTO;
import com.bank.phishaid.initialization.dto.RelayHopDTO;
import com.bank.phishaid.exception.EmailParseException;
import com.bank.phishaid.initialization.serviceLayer.interfaces.IEmailParserService;

import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Parses a raw MIME email into an EmailDTO using Jakarta/Angus Mail.

@Service
public class EmailParserService implements IEmailParserService {

    private static final Logger log = LoggerFactory.getLogger(EmailParserService.class);

    private static final Pattern IP_PATTERN = Pattern.compile(
            "(?:(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)"
                    + "|(?:[0-9a-fA-F]{1,4}:){2,7}[0-9a-fA-F]{1,4}");
    private static final Pattern SPF_PATTERN = Pattern.compile("\\bspf=(\\w+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern DKIM_PATTERN = Pattern.compile("\\bdkim=(\\w+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern DMARC_PATTERN = Pattern.compile("\\bdmarc=(\\w+)", Pattern.CASE_INSENSITIVE);

    @Override
    public EmailDTO Parse(String rawMail) {
        if (rawMail == null || rawMail.isBlank()) {
            throw new EmailParseException("rawMail must not be empty", null);
        }
        try {
            MimeMessage message = new MimeMessage(
                    Session.getDefaultInstance(new Properties()),
                    new ByteArrayInputStream(rawMail.getBytes(StandardCharsets.UTF_8)));

            EmailHeaderDTO head = parseHeader(message);

            StringBuilder bodyText = new StringBuilder();
            List<EmailAttachmentDTO> attachments = new ArrayList<>();
            walkParts(message, bodyText, attachments);

            EmailDTO dto = new EmailDTO();
            dto.setHead(head);
            dto.setBody(bodyText.toString());
            dto.setAttachments(attachments);

            log.info("Parsed raw email into EmailDTO ({} relay hop(s), {} attachment(s))",
                    head.getRelayChain().size(), attachments.size());
            return dto;
        } catch (MessagingException | IOException e) {
            throw new EmailParseException("Failed to parse raw email", e);
        }
    }

    private EmailHeaderDTO parseHeader(MimeMessage message) throws MessagingException {
        EmailHeaderDTO head = new EmailHeaderDTO();
        head.setPhFrom(singleHeader(message, "From"));
        head.setRcpt(singleHeader(message, "To"));
        head.setSub(decode(singleHeader(message, "Subject")));
        head.setRepTo(singleHeader(message, "Reply-To"));
        head.setRetPath(singleHeader(message, "Return-Path"));

        Date sentDate = message.getSentDate();
        head.setTimestampMail(sentDate != null ? sentDate.toInstant() : null);

        List<RelayHopDTO> relayChain = parseRelayChain(message);
        head.setRelayChain(relayChain);
        head.setSenderIp(extractSenderIp(relayChain));

        String authResults = singleHeader(message, "Authentication-Results");
        head.setMailSpf(extractAuthResult(authResults, SPF_PATTERN));
        head.setMailDkim(extractAuthResult(authResults, DKIM_PATTERN));
        head.setMailDmarc(extractAuthResult(authResults, DMARC_PATTERN));

        return head;
    }

    //Returns only the first occurrence of a header, or null if absent
    private String singleHeader(MimeMessage message, String name) throws MessagingException {
        return message.getHeader(name, null);
    }

    private String decode(String rawHeaderValue) {
        if (rawHeaderValue == null) {
            return null;
        }
        try {
            return MimeUtility.decodeText(rawHeaderValue);
        } catch (UnsupportedEncodingException e) {
            return rawHeaderValue;
        }
    }

    private List<RelayHopDTO> parseRelayChain(MimeMessage message) throws MessagingException {
        String[] received = message.getHeader("Received");
        List<RelayHopDTO> chain = new ArrayList<>();
        if (received == null) {
            return chain;
        }
        for (int i = 0; i < received.length; i++) {
            chain.add(new RelayHopDTO(i + 1, received[i]));
        }
        return chain;
    }

    private String extractSenderIp(List<RelayHopDTO> relayChain) {
        if (relayChain.isEmpty()) {
            return null;
        }
        String oldestHop = relayChain.get(relayChain.size() - 1).getHopDescription();
        Matcher matcher = IP_PATTERN.matcher(oldestHop);
        return matcher.find() ? matcher.group() : null;
    }

    private Boolean extractAuthResult(String authResults, Pattern pattern) {
        if (authResults == null) {
            return null;
        }
        Matcher matcher = pattern.matcher(authResults);
        if (!matcher.find()) {
            return null;
        }
        return "pass".equalsIgnoreCase(matcher.group(1));
    }

    private void walkParts(Part part, StringBuilder bodyText, List<EmailAttachmentDTO> attachments)
            throws MessagingException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                walkParts(multipart.getBodyPart(i), bodyText, attachments);
            }
            return;
        }

        if (isAttachment(part)) {
            attachments.add(new EmailAttachmentDTO(part.getFileName(), readBytes(part)));
            return;
        }

        if (part.isMimeType("text/plain") || part.isMimeType("text/html")) {
            Object content = part.getContent();
            if (content != null) {
                bodyText.append(content).append('\n');
            }
        }
    }

    private boolean isAttachment(Part part) throws MessagingException {
        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
            return true;
        }
        return part.getFileName() != null && !part.isMimeType("text/plain") && !part.isMimeType("text/html");
    }

    private byte[] readBytes(Part part) throws MessagingException, IOException {
        try (InputStream in = part.getInputStream()) {
            return in.readAllBytes();
        }
    }
}
