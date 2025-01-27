package com.fernando.ms.followers.app.infrastructure.adapter.output.restclient.client;

import com.fernando.ms.followers.app.domain.models.User;
import com.fernando.ms.followers.app.infrastructure.adapter.output.restclient.models.response.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserWebClient {
    Mono<Boolean> verify(Long id);
    Flux<UserResponse>  findByIds(List<Long> ids);
    Mono<UserResponse> findById(Long id);
}
