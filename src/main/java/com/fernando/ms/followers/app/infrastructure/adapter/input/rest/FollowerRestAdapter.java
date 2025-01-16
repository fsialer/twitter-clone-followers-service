package com.fernando.ms.followers.app.infrastructure.adapter.input.rest;

import com.fernando.ms.followers.app.application.ports.input.FollowerInputPort;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.mapper.FollowerRestMapper;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.response.QuantityFollowerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/followers")
public class FollowerRestAdapter {
    private final FollowerInputPort followerInputPort;
    private final FollowerRestMapper followerRestMapper;

    @GetMapping("/quantity/{followerId}/follower")
    public Mono<ResponseEntity<QuantityFollowerResponse>> quantityFollowers(@PathVariable("followerId") Long followerId){
        return  followerInputPort.quantityFollowers(followerId)
                .flatMap(follower->{
                    return Mono.just(ResponseEntity.ok().body(followerRestMapper.toQuantityFollowerResponse(follower)));
                });
    }
}
