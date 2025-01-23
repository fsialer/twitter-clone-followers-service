package com.fernando.ms.followers.app.infrastructure.adapter.output.persistence;

import com.fernando.ms.followers.app.application.ports.output.FollowerPersistencePort;
import com.fernando.ms.followers.app.domain.models.Follower;
import com.fernando.ms.followers.app.infrastructure.adapter.output.persistence.mapper.FollowerPersistenceMapper;
import com.fernando.ms.followers.app.infrastructure.adapter.output.persistence.models.FollowerDocument;
import com.fernando.ms.followers.app.infrastructure.adapter.output.persistence.repository.FollowerReactiveMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class FollowerPersistenceAdapter implements FollowerPersistencePort {

    private final FollowerReactiveMongoRepository followerReactiveMongoRepository;
    private final FollowerPersistenceMapper followerPersistenceMapper;

    @Override
    public Flux<Follower> findFollowers(Long followerId) {
        return followerPersistenceMapper.toFollowers(followerReactiveMongoRepository.findAllByFollowerId(followerId));
    }

    @Override
    public Mono<Follower> save(Follower follower) {
        FollowerDocument followerDocument=followerPersistenceMapper.toFollowerDocument(follower);
        followerDocument.setFollowerId(follower.getFollower().getId());
        followerDocument.setFollowedId(follower.getFollowed().getId());
        followerDocument.setCreateAt(LocalDateTime.now());
        return followerPersistenceMapper.toFollower(followerReactiveMongoRepository.save(followerDocument));
    }

    @Override
    public Mono<Follower> findByFollowedId(String id) {
        return followerReactiveMongoRepository.findByFollowedId(id).map(followerPersistenceMapper::toFollower);
    }

    @Override
    public Mono<Void> delete(String id) {
        return followerReactiveMongoRepository.deleteById(id);
    }

    @Override
    public Mono<Boolean> existsByFollowerIdFollowedId(Long followerId, Long followedId) {
        return followerReactiveMongoRepository.existsByFollowerIdAndFollowedId(followerId,followedId);
    }
}
