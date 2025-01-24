package com.fernando.ms.followers.app.application.ports.output;

import com.fernando.ms.followers.app.domain.models.Follower;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FollowerPersistencePort {
    Flux<Follower> findFollowers(Long followerId);
    Mono<Follower> save(Follower follower);
    Mono<Follower> findFollowedById(Long followedId);
    Mono<Void> delete(String id);
    Mono<Boolean> existsByFollowerIdFollowedId(Long followerId,Long followedId);
    Flux<Follower> findFollowersPaginated(Long followerId,Long page,Long size);
    Flux<Follower> findAllFollowedByFollowerPaginated(Long followerId);
}
