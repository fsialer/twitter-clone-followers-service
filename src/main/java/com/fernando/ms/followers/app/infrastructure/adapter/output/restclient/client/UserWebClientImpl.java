package com.fernando.ms.followers.app.infrastructure.adapter.output.restclient.client;

import com.fernando.ms.followers.app.domain.models.User;
import com.fernando.ms.followers.app.infrastructure.adapter.output.restclient.models.response.ExistsUserResponse;
import com.fernando.ms.followers.app.infrastructure.adapter.output.restclient.models.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserWebClientImpl implements UserWebClient{
    private final WebClient webClientUser;

    @Override
    public Mono<Boolean> verify(Long id) {
        return webClientUser
                .get()
                .uri("/{id}/verify",id)
                .retrieve()
                .bodyToMono(ExistsUserResponse.class)
                .flatMap(existsPostResponse -> {
                    return Mono.just(existsPostResponse.getExists());
                });
    }

    @Override
    public Flux<UserResponse> findByIds(List<Long> ids) {
        return webClientUser
                .get()
                .uri(uriBuilder -> uriBuilder.path("/find-by-ids").queryParam("ids", ids).build())
                .retrieve()
                .bodyToFlux(UserResponse.class);
    }

    @Override
    public Mono<UserResponse> findById(Long id) {
        return webClientUser
                .get()
                .uri("/{id}",id)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }
}
