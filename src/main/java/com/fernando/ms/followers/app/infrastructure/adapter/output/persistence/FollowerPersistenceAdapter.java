package com.fernando.ms.followers.app.infrastructure.adapter.output.persistence;

import com.fernando.ms.followers.app.application.ports.output.FollowerPersistencePort;
import com.fernando.ms.followers.app.domain.models.Follower;
import com.fernando.ms.followers.app.infrastructure.adapter.output.persistence.mapper.FollowerPersistenceMapper;
import com.fernando.ms.followers.app.infrastructure.adapter.output.persistence.repository.FollowerReactiveMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class FollowerPersistenceAdapter implements FollowerPersistencePort {

    private final FollowerReactiveMongoRepository followerReactiveMongoRepository;
    private final FollowerPersistenceMapper followerPersistenceMapper;

    @Override
    public Flux<Follower> findAllByFollowerId(Long followerId) {
        return followerPersistenceMapper.toFollowers(followerReactiveMongoRepository.findAllByFollowerId(followerId));
    }
}
