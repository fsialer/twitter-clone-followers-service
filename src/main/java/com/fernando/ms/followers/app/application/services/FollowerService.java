package com.fernando.ms.followers.app.application.services;

import com.fernando.ms.followers.app.application.ports.input.FollowerInputPort;
import com.fernando.ms.followers.app.application.ports.output.FollowerPersistencePort;
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
}
