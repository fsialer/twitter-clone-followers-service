package com.fernando.ms.followers.app.infrastructure.adapter.output.restclient;

import com.fernando.ms.followers.app.Utils.TestUtilsUser;
import com.fernando.ms.followers.app.domain.models.User;
import com.fernando.ms.followers.app.infrastructure.adapter.output.restclient.client.UserWebClient;
import com.fernando.ms.followers.app.infrastructure.adapter.output.restclient.mapper.UserRestClientMapper;
import com.fernando.ms.followers.app.infrastructure.adapter.output.restclient.models.response.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserRestClientAdapterTest {
    @Mock
    private UserWebClient userWebClient;
    @Mock
    private UserRestClientMapper userRestClientMapper;

    @InjectMocks
    private UserRestClientAdapter userRestClientAdapter;

    @Test
    @DisplayName("When UserId Is Correct Expect True if User Exists")
    void When_UserIdIsCorrect_Expect_TrueIfUserExists(){
        when(userWebClient.verify(anyLong())).thenReturn(Mono.just(Boolean.TRUE));

        Mono<Boolean> exists=userRestClientAdapter.verify(1L);

        StepVerifier.create(exists)
                .expectNext(true)
                .verifyComplete();
        Mockito.verify(userWebClient,times(1)).verify(anyLong());
    }

    @Test
    @DisplayName("When UserId Is Not Correct Expect False if User Exists")
    void When_UserIdIsNotCorrect_Expect_FalseIfUserExists(){
        when(userWebClient.verify(anyLong())).thenReturn(Mono.just(Boolean.FALSE));

        Mono<Boolean> exists=userRestClientAdapter.verify(1L);

        StepVerifier.create(exists)
                .expectNext(false)
                .verifyComplete();
        Mockito.verify(userWebClient,times(1)).verify(anyLong());
    }

    @Test
    @DisplayName("When List Users Are Correct Expect A List Users Correct")
    void When_ListUsersAreCorrect_Expect_AListUsersCorrect(){
        UserResponse userResponse= TestUtilsUser.buildUserResponseMock();
        User user=TestUtilsUser.buildUserMock();
        when(userWebClient.findByIds(anyList())).thenReturn(Flux.just(userResponse));
        when(userRestClientMapper.toUser(any(UserResponse.class))).thenReturn(user);

        Flux<User> users=userRestClientAdapter.findByIds(List.of(1L));
        StepVerifier.create(users)
                .expectNext(user)
                .verifyComplete();
        Mockito.verify(userWebClient,times(1)).findByIds(anyList());
        Mockito.verify(userRestClientMapper,times(1)).toUser(any(UserResponse.class));
    }

    @Test
    @DisplayName("When UserId Is Correct Expect User Information Correct")
    void When_UserIdIsCorrect_Expect_UserInformationCorrect(){
        UserResponse userResponse= TestUtilsUser.buildUserResponseMock();
        User user=TestUtilsUser.buildUserMock();
        when(userWebClient.findById(anyLong())).thenReturn(Mono.just(userResponse));
        when(userRestClientMapper.toUser(any(UserResponse.class))).thenReturn(user);
        Mono<User> userInfo=userRestClientAdapter.findById(1L);

        StepVerifier.create(userInfo)
                .expectNext(user)
                .verifyComplete();

        Mockito.verify(userWebClient,times(1)).findById(anyLong());
        Mockito.verify(userRestClientMapper,times(1)).toUser(any(UserResponse.class));
    }
}
