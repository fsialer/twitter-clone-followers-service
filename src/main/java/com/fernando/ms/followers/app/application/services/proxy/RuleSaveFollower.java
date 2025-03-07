package com.fernando.ms.followers.app.application.services.proxy;

import com.fernando.ms.followers.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.followers.app.application.ports.output.FollowerPersistencePort;
import com.fernando.ms.followers.app.domain.exception.FollowedNotFoundException;
import com.fernando.ms.followers.app.domain.exception.FollowerNotFoundException;
import com.fernando.ms.followers.app.domain.exception.FollowerRuleException;
import com.fernando.ms.followers.app.domain.models.Follower;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RuleSaveFollower implements IProcess{
    private final FollowerPersistencePort followerPersistencePort;
    private final ExternalUserOutputPort externalUserOutputPort;

    @Override
    public Mono<Follower> doProcess(Follower follower) {
        return followerPersistencePort
                .existsByFollowerIdFollowedId(follower.getFollower().getId() ,follower.getFollowed().getId())
                .filter(Boolean.FALSE::equals)
                .switchIfEmpty(Mono.error(new FollowerRuleException("You are follower this user.")))
                .flatMap(existsFollowerUnique->externalUserOutputPort.verify(follower.getFollower().getId())
                        .filter(Boolean.TRUE::equals)
                        .switchIfEmpty(Mono.error(FollowerNotFoundException::new))
                        .flatMap(existsFollower->externalUserOutputPort.verify(follower.getFollowed().getId())
                                .filter(Boolean.TRUE::equals)
                                .switchIfEmpty(Mono.error(FollowedNotFoundException::new))
                                .flatMap(existsFollowed->Mono.just(follower))
                        )
                );
    }
}
