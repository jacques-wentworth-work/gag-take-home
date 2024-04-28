package com.wentworth.gagtakehome.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private final WebClient.Builder webClientBuilder;

    public WebClientConfig(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Bean
    public WebClient ipWebClient(@Value("${ip-api.url}") String url) {
        return webClientBuilder
                .baseUrl(url)
                .build();
    }
}
