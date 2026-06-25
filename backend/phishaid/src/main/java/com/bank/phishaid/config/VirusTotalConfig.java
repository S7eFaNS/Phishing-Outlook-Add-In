package com.bank.phishaid.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class VirusTotalConfig {

    private static final String VT_BASE_URL = "https://www.virustotal.com/api/v3";

    @Bean
    RestClient virusTotalRestClient(@Value("${VT_API_KEY:}") String apiKey) {
        return RestClient.builder()
                .baseUrl(VT_BASE_URL)
                .defaultHeader("x-apikey", apiKey)
                .build();
    }
}
