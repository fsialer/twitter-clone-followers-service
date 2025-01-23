package com.fernando.ms.followers.app.application.ports.input;

import com.fernando.ms.followers.app.domain.models.Follower;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FollowerInputPort {
    Mono<Long> quantityFollowers(Long followerId);
    Mono<Follower> save(Follower follower);
    Mono<Void> unfollow(Long followerId,Long followedId);
}
