package com.fernando.ms.followers.app.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.followers.app.Utils.TestUtilsFollower;
import com.fernando.ms.followers.app.application.ports.input.FollowerInputPort;
import com.fernando.ms.followers.app.domain.models.Follower;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.mapper.FollowerRestMapper;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.request.CreateFollowerRequest;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.response.FollowerResponse;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.response.QuantityFollowerResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = {FollowerRestAdapter.class})
public class FollowerRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private FollowerInputPort followerInputPort;

    @MockBean
    private FollowerRestMapper followerRestMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("When Follower Exists Expect Quantity Followers Successfully")
    void When_FollowerExists_Expect_QuantityFollowersSuccessfully() {
        QuantityFollowerResponse quantityFollowerResponse= TestUtilsFollower.buildQuantityFollowerResponseMock();
        when(followerInputPort.quantityFollowers(anyLong())).thenReturn(Mono.just(1L));
        when(followerRestMapper.toQuantityFollowerResponse(anyLong())).thenReturn(quantityFollowerResponse);

        webTestClient.get()
                .uri("/followers/quantity/{followerId}/follower",1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.quantity").isEqualTo(1L);

        Mockito.verify(followerInputPort, times(1)).quantityFollowers(anyLong());
        Mockito.verify(followerRestMapper, times(1)).toQuantityFollowerResponse(anyLong());

    }

    @Test
    @DisplayName("When Save Follower Expect Follower Saved Successfully")
    void when_SaveFollower_Expect_FollowerSavedSuccessfully() throws JsonProcessingException {
        CreateFollowerRequest createFollowerRequest = TestUtilsFollower.buildCreateFollowerRequestMock();
        FollowerResponse followerResponse = TestUtilsFollower.buildFollowerResponseMock();

        when(followerInputPort.save(any())).thenReturn(Mono.just(TestUtilsFollower.buildFollowerMock()));
        when(followerRestMapper.toFollower(any(CreateFollowerRequest.class))).thenReturn(TestUtilsFollower.buildFollowerMock());
        when(followerRestMapper.toFollowerResponse(any(Follower.class))).thenReturn(followerResponse);

        webTestClient.post()
                .uri("/followers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(createFollowerRequest))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(followerResponse.getId());

        Mockito.verify(followerInputPort, times(1)).save(any());
        Mockito.verify(followerRestMapper, times(1)).toFollower(any(CreateFollowerRequest.class));
        Mockito.verify(followerRestMapper, times(1)).toFollowerResponse(any(Follower.class));
    }

    @Test
    @DisplayName("When Unfollow Is Called With Valid FollowerId And FollowedId Expect No Content Status")
    void when_UnfollowIsCalledWithValidFollowerIdAndFollowedId_Expect_NoContentStatus() {
        Long followerId = 1L;
        Long followedId = 2L;

        when(followerInputPort.unfollow(anyLong(), anyLong())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/followers/unfollow/{followerId}/follower/{followedId}/followed", followerId, followedId)
                .exchange()
                .expectStatus().isNoContent();

        Mockito.verify(followerInputPort, times(1)).unfollow(anyLong(), anyLong());
    }


}
