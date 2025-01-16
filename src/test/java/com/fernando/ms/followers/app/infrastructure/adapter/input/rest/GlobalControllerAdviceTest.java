package com.fernando.ms.followers.app.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.followers.app.Utils.TestUtilsFollower;
import com.fernando.ms.followers.app.application.ports.input.FollowerInputPort;
import com.fernando.ms.followers.app.domain.exception.FollowedNotFoundException;
import com.fernando.ms.followers.app.domain.exception.FollowerNotFoundException;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.mapper.FollowerRestMapper;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.request.CreateFollowerRequest;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.response.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.enums.ErrorType.FUNCTIONAL;
import static com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.enums.ErrorType.SYSTEM;
import static com.fernando.ms.followers.app.infrastructure.utils.ErrorCatalog.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = {FollowerRestAdapter.class})
public class GlobalControllerAdviceTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private FollowerInputPort followerInputPort;

    @MockBean
    private FollowerRestMapper followerRestMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("When Exception Occurs Expect Internal Server Error Response")
    void handleExceptionTest() throws JsonProcessingException {
        CreateFollowerRequest createFollowerRequest= TestUtilsFollower.buildCreateFollowerRequestMock();
        //createFollowerRequest.setFollowedId(null);
        when(followerRestMapper.toFollower(any(CreateFollowerRequest.class))).thenReturn(TestUtilsFollower.buildFollowerMock());
        when(followerInputPort.save(any())).thenReturn(Mono.error(new RuntimeException("Unexpected error")));

        webTestClient.post()
                .uri("/followers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(createFollowerRequest))
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(INTERNAL_SERVER_ERROR.getCode());
                    assert response.getType().equals(SYSTEM);
                    assert response.getMessage().equals(INTERNAL_SERVER_ERROR.getMessage());
                    assert response.getDetails().equals(Collections.singletonList("Unexpected error"));
                });
        Mockito.verify(followerInputPort, times(1)).save(any());
        Mockito.verify(followerRestMapper, times(1)).toFollower(any(CreateFollowerRequest.class));
        Mockito.verify(followerRestMapper, times(0)).toFollowerResponse(any());
    }

    @Test
    @DisplayName("Expect WebExchangeBindException When Follower Information Is Invalid")
    void Expect_WebExchangeBindException_When_FollowerInformationIsInvalid() throws JsonProcessingException {
        CreateFollowerRequest createFollowerRequest= TestUtilsFollower.buildCreateFollowerRequestMock();
        createFollowerRequest.setFollowedId(null);

        webTestClient.post()
                .uri("/followers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(createFollowerRequest))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(FOLLOWER_BAD_PARAMETERS.getCode());
                    assert response.getMessage().equals(FOLLOWER_BAD_PARAMETERS.getMessage());
                });
    }

    @Test
    @DisplayName("When FollowerNotFoundException is thrown, expect BAD_REQUEST status and correct error response")
    void when_FollowerNotFoundExceptionThrown_Expect_BadRequestAndCorrectErrorResponse() {
        FollowerNotFoundException exception = new FollowerNotFoundException();
        Long followerId = 1L;
        Long followedId = 2L;
        when(followerInputPort.unfollow(anyLong(),anyLong())).thenReturn(Mono.error(exception));
        webTestClient.delete()
                .uri("/followers/unfollow/{followerId}/follower/{followedId}/followed", followerId, followedId)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(FOLLOWER_NOT_FOUND.getCode());
                    assert response.getType().equals(FUNCTIONAL);
                    assert response.getMessage().equals(FOLLOWER_NOT_FOUND.getMessage());
                });
    }

    @Test
    @DisplayName("When FollowedNotFoundException is thrown, expect BAD_REQUEST status and correct error response")
    void when_FollowedNotFoundExceptionThrown_Expect_BadRequestAndCorrectErrorResponse() {
        Long followerId = 1L;
        Long followedId = 2L;
        FollowedNotFoundException exception = new FollowedNotFoundException();
        when(followerInputPort.unfollow(anyLong(),anyLong())).thenReturn(Mono.error(exception));
        webTestClient.delete()
                .uri("/followers/unfollow/{followerId}/follower/{followedId}/followed", followerId, followedId)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(FOLLOWED_NOT_FOUND.getCode());
                    assert response.getType().equals(FUNCTIONAL);
                    assert response.getMessage().equals(FOLLOWED_NOT_FOUND.getMessage());
                });
    }

}
