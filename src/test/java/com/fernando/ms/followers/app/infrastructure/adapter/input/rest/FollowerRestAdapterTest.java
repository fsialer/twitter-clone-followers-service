package com.fernando.ms.followers.app.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.followers.app.Utils.TestUtilsFollower;
import com.fernando.ms.followers.app.application.ports.input.FollowerInputPort;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.mapper.FollowerRestMapper;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.response.QuantityFollowerResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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

}
