package com.fernando.ms.followers.app.application.ports.output;

import reactor.core.publisher.Mono;

public interface ExternalUserOutputPort {
    Mono<Boolean> verify(Long id);
}
