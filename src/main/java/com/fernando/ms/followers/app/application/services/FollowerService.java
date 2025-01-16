package com.fernando.ms.followers.app.application.services;

import com.fernando.ms.followers.app.application.ports.input.FollowerInputPort;
import com.fernando.ms.followers.app.application.ports.output.FollowerPersistencePort;
import com.fernando.ms.followers.app.domain.exception.FollowedNotFoundException;
import com.fernando.ms.followers.app.domain.exception.FollowerNotFoundException;
import com.fernando.ms.followers.app.domain.models.Follower;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FollowerService implements FollowerInputPort {
    private final FollowerPersistencePort followerPersistencePort;
    @Override
    public Mono<Long> quantityFollowers(Long followerId) {
        return followerPersistencePort.findAllByFollowerId(followerId)
                .count();
    }

    @Override
    public Mono<Follower> save(Follower follower) {
        return followerPersistencePort.save(follower);
    }

    @Override
    public Mono<Void> unfollow(Long followerId, Long followedId) {
        return followerPersistencePort.findAllByFollowerId(followerId)
                .switchIfEmpty(Mono.error(new FollowerNotFoundException()))
                .filter(existFollower -> existFollower.getFollowed().getId().equals(followedId))
                .switchIfEmpty(Mono.error(new FollowedNotFoundException()))
                .single()
                .flatMap(existFollowed -> followerPersistencePort.delete(existFollowed.getId()));
    }
}
