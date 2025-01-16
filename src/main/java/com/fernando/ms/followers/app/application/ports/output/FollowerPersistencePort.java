package com.fernando.ms.followers.app.application.ports.output;

import com.fernando.ms.followers.app.domain.models.Follower;
import reactor.core.publisher.Flux;

public interface FollowerPersistencePort {
    Flux<Follower> findAllByFollowerId(Long followerId);
}
