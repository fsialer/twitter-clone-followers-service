package com.fernando.ms.followers.app.infrastructure.adapter.input.rest.mapper;

import com.fernando.ms.followers.app.domain.models.Follower;
import com.fernando.ms.followers.app.domain.models.User;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.request.CreateFollowerRequest;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.response.FollowerResponse;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.response.QuantityFollowerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FollowerRestMapper {
    @Mapping(target = "quantity",expression = "java(mapQuantity(quantity))")
    QuantityFollowerResponse toQuantityFollowerResponse(Long quantity);

    default Long mapQuantity(Long quantity){
        return  quantity;
    }

    QuantityFollowerResponse toQuantityFollowerResponse(Follower follower);

    @Mapping(target = "follower",expression = "java(mapFollower(rq))")
    @Mapping(target = "followed",expression = "java(mapFollowed(rq))")
    Follower toFollower(CreateFollowerRequest rq);

    default User mapFollower(CreateFollowerRequest rq){
        return User.builder().id(rq.getFollowerId()).build();
    }

    default User mapFollowed(CreateFollowerRequest rq){
        return User.builder().id(rq.getFollowedId()).build();
    }

    @Mapping(target = "follower",expression = "java(mapFollowerId(follower))")
    @Mapping(target = "followed",expression = "java(mapFollowedId(follower))")
    FollowerResponse toFollowerResponse(Follower follower);

    default Long mapFollowedId(Follower follower){
        return follower.getFollowed().getId();
    }

    default Long mapFollowerId(Follower follower){
        return follower.getFollower().getId();
    }
}
