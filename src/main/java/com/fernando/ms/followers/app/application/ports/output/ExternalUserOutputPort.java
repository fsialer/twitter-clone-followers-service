package com.fernando.ms.followers.app.application.ports.output;

import com.fernando.ms.followers.app.domain.models.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ExternalUserOutputPort {
    Mono<Boolean> verify(Long id);
    Flux<User> findByIds(List<Long> ids);
    Mono<User> findById(Long id);
}
