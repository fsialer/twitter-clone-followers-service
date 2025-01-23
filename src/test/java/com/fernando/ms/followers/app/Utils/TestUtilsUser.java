package com.fernando.ms.followers.app.Utils;

import com.fernando.ms.followers.app.domain.models.User;

public class TestUtilsUser {
    public static User buildUserMock(){
        return User.builder()
                .id(1L)
                .names("Fernando")
                .build();
    }
}
