package com.fernando.ms.followers.app.application.ports.input;

import reactor.core.publisher.Mono;

public interface FollowerInputPort {
    Mono<Long> quantityFollowers(Long followerId);
}
