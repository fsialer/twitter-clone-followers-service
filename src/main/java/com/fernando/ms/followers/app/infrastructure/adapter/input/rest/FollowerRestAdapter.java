package com.fernando.ms.followers.app.infrastructure.adapter.input.rest;

import com.fernando.ms.followers.app.application.ports.input.FollowerInputPort;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.mapper.FollowerRestMapper;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.request.CreateFollowerRequest;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.response.FollowResponse;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.response.FollowerResponse;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.response.QuantityFollowerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

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

    @PostMapping
    public Mono<ResponseEntity<FollowResponse>> save(@Valid @RequestBody CreateFollowerRequest rq){
        return followerInputPort.save(followerRestMapper.toFollower(rq))
                .flatMap(follower -> {
                    String location="/followers/".concat(follower.getId());
                    return Mono.just(ResponseEntity.created(URI.create(location)).body(followerRestMapper.toFollowResponse(follower)));
                });
    }

    @DeleteMapping("/unfollow/{followerId}/follower/{followedId}/followed")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> unfollow(@PathVariable("followerId") Long followerId,@PathVariable("followedId") Long followedId){
        return followerInputPort.unfollow(followerId,followedId);
    }

    @GetMapping("/{followerId}/paginated")
    public Flux<FollowerResponse> findFollowersPaginated(@PathVariable("followerId") Long followerId,
            @RequestParam(name = "size",required = false,defaultValue = "10") Long size,
            @RequestParam(name = "page",required = false,defaultValue = "0") Long page){
        return followerRestMapper.toFollowersResponse(followerInputPort.findFollowersPaginated(followerId,page,size));
    }

    @GetMapping("/find-followed-by-follower/{followerId}")
    public Flux<FollowResponse> findAllFollowedByFollower(@PathVariable("followerId") Long followerId){
        return followerRestMapper.toFollowsResponse(followerInputPort.findAllFollowedByFollower(followerId));
    }

    @GetMapping("/{followerId}")
    public Flux<FollowerResponse> findFollowers(@PathVariable("followerId") Long followerId){
        return followerRestMapper.toFollowersResponse(followerInputPort.findFollowers(followerId));
    }


}
