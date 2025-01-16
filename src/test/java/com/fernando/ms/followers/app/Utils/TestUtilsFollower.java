package com.fernando.ms.followers.app.Utils;

import com.fernando.ms.followers.app.domain.models.Follower;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.response.QuantityFollowerResponse;
import com.fernando.ms.followers.app.infrastructure.adapter.output.persistence.models.FollowerDocument;

import java.time.LocalDateTime;

public class TestUtilsFollower {

    public static Follower buildFollowerMock(){
        return Follower.builder()
                .id("1")
                .followerId(1L)
                .followedId(2L)
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
}
