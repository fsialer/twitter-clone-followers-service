package com.fernando.ms.followers.app.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
//@RequiredArgsConstructor
public class WebClientConfig {
    @Value("${users-service.url}")
    private String apiUser;

    @Bean
    public WebClient webClientUser(WebClient.Builder builder) {
        return builder.baseUrl(apiUser).build();
    }
}
