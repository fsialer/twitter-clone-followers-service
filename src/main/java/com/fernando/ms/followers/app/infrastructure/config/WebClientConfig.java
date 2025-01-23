package com.fernando.ms.followers.app.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClientUser(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8080").build();
    }
}
