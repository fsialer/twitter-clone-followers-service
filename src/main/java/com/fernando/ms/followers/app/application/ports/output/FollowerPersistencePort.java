package com.fernando.ms.followers.app.application.ports.output;

import com.fernando.ms.followers.app.domain.models.Follower;
import com.fernando.ms.followers.app.domain.models.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FollowerPersistencePort {
    Flux<Follower> findFollowers(Long followerId);
    Mono<Follower> save(Follower follower);
    Mono<Follower> findByFollowedId(String id);
    Mono<Void> delete(String id);
    Mono<Boolean> existsByFollowerIdFollowedId(Long followerId,Long followedId);
    Flux<Follower> findFollowersPaginated(Long followerId,Long page,Long size);
}
