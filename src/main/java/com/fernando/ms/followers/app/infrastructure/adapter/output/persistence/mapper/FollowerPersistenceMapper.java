package com.fernando.ms.followers.app.infrastructure.adapter.output.persistence.mapper;

import com.fernando.ms.followers.app.domain.models.Follower;
import com.fernando.ms.followers.app.domain.models.User;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.request.CreateFollowerRequest;
import com.fernando.ms.followers.app.infrastructure.adapter.output.persistence.models.FollowerDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring")
public interface FollowerPersistenceMapper {
    default Flux<Follower> toFollowers(Flux<FollowerDocument> followers){
        return followers.map(this::toFollower);
    }

    default Mono<Follower> toFollower(Mono<FollowerDocument> follower){
        return follower.map(this::toFollower);
    }

    @Mapping(target = "follower",expression = "java(mapFollower(follower))")
    @Mapping(target = "followed",expression = "java(mapFollowed(follower))")
    Follower toFollower(FollowerDocument follower);

    FollowerDocument toFollowerDocument(Follower follower);

    default User mapFollower(FollowerDocument follower){
        return User.builder().id(follower.getFollowerId()).build();
    }

    default User mapFollowed(FollowerDocument follower){
        return User.builder().id(follower.getFollowedId()).build();
    }
}
