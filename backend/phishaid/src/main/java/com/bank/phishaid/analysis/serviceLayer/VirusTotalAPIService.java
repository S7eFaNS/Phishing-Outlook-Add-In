package com.bank.phishaid.analysis.serviceLayer;

import com.bank.phishaid.analysis.dto.Verdict;
import com.bank.phishaid.analysis.serviceLayer.interfaces.IUrlAndAttachmentChecker;

import tools.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
class VirusTotalAPIService implements IUrlAndAttachmentChecker {

    private static final Logger log = LoggerFactory.getLogger(VirusTotalAPIService.class);

    private final RestClient client;

    VirusTotalAPIService(RestClient virusTotalRestClient) {
        this.client = virusTotalRestClient;
    }

    //url lookup
    @Override
    public Verdict UrlCheck(String url) {
        String urlId = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(url.getBytes(StandardCharsets.UTF_8));
        return lookup("/urls/" + urlId, "URL");
    }

    //file-hash lookup.
    @Override
    public Verdict AttCheck(String att) {
        return lookup("/files/" + att, "attachment");
    }

    private Verdict lookup(String path, String kind) {
        try {
            JsonNode body = client.get().uri(path).retrieve().body(JsonNode.class);
            if (body == null) {
                log.warn("VirusTotal {} lookup returned an empty body; treating as unavailable", kind);
                return Verdict.unavailable();
            }
            JsonNode stats = body.path("data").path("attributes").path("last_analysis_stats");
            int malicious = stats.path("malicious").asInt(0);
            int suspicious = stats.path("suspicious").asInt(0);
            boolean isMalicious = malicious > 0 || suspicious > 0;
            log.debug("VirusTotal {} verdict: malicious={}, suspicious={}", kind, malicious, suspicious);
            return new Verdict(isMalicious, malicious, suspicious, true);
        } catch (RestClientException e) {
            log.warn("VirusTotal {} lookup unavailable: {}", kind, e.getMessage());
            return Verdict.unavailable();
        }
    }
}
