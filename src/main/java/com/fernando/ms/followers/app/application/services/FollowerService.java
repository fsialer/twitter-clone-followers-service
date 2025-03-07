package com.fernando.ms.followers.app.application.services;

import com.fernando.ms.followers.app.application.ports.input.FollowerInputPort;
import com.fernando.ms.followers.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.followers.app.application.ports.output.FollowerPersistencePort;
import com.fernando.ms.followers.app.application.services.proxy.IProcess;
import com.fernando.ms.followers.app.application.services.proxy.ProcessFactory;
import com.fernando.ms.followers.app.domain.exception.FollowedNotFoundException;
import com.fernando.ms.followers.app.domain.exception.FollowerNotFoundException;
import com.fernando.ms.followers.app.domain.exception.FollowerRuleException;
import com.fernando.ms.followers.app.domain.models.Follower;
import com.fernando.ms.followers.app.domain.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowerService implements FollowerInputPort {
    private final FollowerPersistencePort followerPersistencePort;
    private final ExternalUserOutputPort externalUserOutputPort;
    @Override
    public Mono<Long> quantityFollowers(Long followerId) {
        return followerPersistencePort.findFollowers(followerId)
                .count();
    }

    @Override
    public Mono<Follower> save(Follower follower) {
        IProcess process= ProcessFactory.validSaveFollower(followerPersistencePort,externalUserOutputPort);
        return process.doProcess(follower).flatMap(followerPersistencePort::save);
    }

    @Override
    public Mono<Void> unfollow(Long followerId, Long followedId) {
        return followerPersistencePort.findFollowers(followerId)
                .switchIfEmpty(Mono.error(new FollowerNotFoundException()))
                .filter(existFollower -> existFollower.getFollowed().getId().equals(followedId))
                .switchIfEmpty(Mono.error(new FollowedNotFoundException()))
                .single()
                .flatMap(existFollowed -> followerPersistencePort.delete(existFollowed.getId()));
    }

    @Override
    public Flux<User> findFollowersPaginated(Long followerId, Long page, Long size) {
        return followerPersistencePort.findFollowersPaginated(followerId,page,size)
                .flatMap(follower -> Flux.just(follower.getFollower().getId()))
                .flatMap(ids-> externalUserOutputPort.findByIds(Collections.singletonList(ids)));
    }

    @Override
    public Flux<Follower> findAllFollowedByFollower(Long followedId) {
        return followerPersistencePort.findAllFollowedByFollowerPaginated(followedId);
    }

    @Override
    public Flux<User> findFollowers(Long followerId) {
        return followerPersistencePort.findFollowers(followerId)
                .flatMap(follower -> Flux.just(follower.getFollower().getId()))
                .flatMap(ids->externalUserOutputPort.findByIds(Collections.singletonList(ids)));
    }
}
