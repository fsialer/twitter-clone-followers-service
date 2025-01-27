package com.fernando.ms.followers.app.Utils;

import com.fernando.ms.followers.app.domain.models.User;
import com.fernando.ms.followers.app.infrastructure.adapter.output.restclient.models.response.UserResponse;

public class TestUtilsUser {
    public static User buildUserMock(){
        return User.builder()
                .id(1L)
                .names("Fernando")
                .build();
    }

    public static UserResponse buildUserResponseMock(){
        return UserResponse.builder()
                .id(1L)
                .username("falexs")
                .names("fernando")
                .email("example@mail.com")
                .build();
    }
}
