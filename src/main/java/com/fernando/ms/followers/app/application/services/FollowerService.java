package com.fernando.ms.followers.app.application.services;

import com.fernando.ms.followers.app.application.ports.input.FollowerInputPort;
import com.fernando.ms.followers.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.followers.app.application.ports.output.FollowerPersistencePort;
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
        return followerPersistencePort.existsByFollowerIdFollowedId(follower.getFollower().getId() ,follower.getFollowed().getId())
                        .flatMap(existsFollowerUnique->{
                            if(Boolean.TRUE.equals(existsFollowerUnique)){
                                return Mono.error(new FollowerRuleException("You are follower this user."));
                            }
                            return externalUserOutputPort.verify(follower.getFollower().getId())
                                    .flatMap(existsFollower->{
                                        if(Boolean.FALSE.equals(existsFollower)){
                                            return Mono.error(FollowerNotFoundException::new);
                                        }
                                        return externalUserOutputPort.verify(follower.getFollowed().getId())
                                                .flatMap(existsFollowed->{
                                                    if(Boolean.FALSE.equals(existsFollowed)){
                                                        return Mono.error(FollowedNotFoundException::new);
                                                    }
                                                    return followerPersistencePort.save(follower);
                                                });
                                    });
                        });

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
                .flatMap(follower -> {
                    return Flux.just(follower.getFollower().getId());
                })
                .flatMap(ids->{
                    return externalUserOutputPort.findByIds(Collections.singletonList(ids));
                });
    }

    @Override
    public Flux<Follower> findAllFollowedByFollower(Long followedId) {
        return followerPersistencePort.findAllFollowedByFollowerPaginated(followedId);
    }
}
