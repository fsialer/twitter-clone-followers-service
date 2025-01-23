package com.fernando.ms.followers.app.Utils;

import com.fernando.ms.followers.app.domain.models.Follower;
import com.fernando.ms.followers.app.domain.models.User;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.request.CreateFollowerRequest;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.response.FollowResponse;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.response.FollowerResponse;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.response.QuantityFollowerResponse;
import com.fernando.ms.followers.app.infrastructure.adapter.output.persistence.models.FollowerDocument;

import java.time.LocalDateTime;

public class TestUtilsFollower {

    public static Follower buildFollowerMock(){
        return Follower.builder()
                .id("1")
                .follower(User.builder().id(1L).build())
                .followed(User.builder().id(2L).build())
                .build();
    }

    public static FollowerDocument buildFollowerDocumentMock(){
        return FollowerDocument.builder()
                .id("1")
                .followerId(1L)
                .followedId(2L)
                .createAt(LocalDateTime.now())
                .build();
    }

    public static QuantityFollowerResponse buildQuantityFollowerResponseMock(){
        return QuantityFollowerResponse.builder()
                .quantity(1L)
                .build();
    }

    public static FollowResponse buildFollowResponseMock(){
        return FollowResponse.builder()
                .id("67894256c864356454574770")
                .followed(1L)
                .follower(2L)
                .build();
    }

    public static CreateFollowerRequest buildCreateFollowerRequestMock(){
        return CreateFollowerRequest.builder()
                .followedId(1L)
                .followerId(2L)
                .build();
    }

    public static FollowerResponse buildFollowerResponseMock(){
        return FollowerResponse.builder()
                .id(1L)
                .names("Fernando")
                .build();
    }
}
