package com.fernando.ms.followers.app.application.services;

import com.fernando.ms.followers.app.Utils.TestUtilsFollower;
import com.fernando.ms.followers.app.application.ports.output.FollowerPersistencePort;
import com.fernando.ms.followers.app.domain.exception.FollowedNotFoundException;
import com.fernando.ms.followers.app.domain.exception.FollowerNotFoundException;
import com.fernando.ms.followers.app.domain.models.Follower;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowerServiceTest {
    @Mock
    private FollowerPersistencePort followerPersistencePort;

    @InjectMocks
    private FollowerService followerService;

    @Test
    @DisplayName("When followerId And TargetType Are Correct Expect Quantity Followers Exists")
    void When_TargetIDAndTargetTypeAreCorrect_Expect_QuantityLikeExists() {
        Follower follower= TestUtilsFollower.buildFollowerMock();

        when(followerPersistencePort.findAllByFollowerId(anyLong())).thenReturn(Flux.just(follower));

        Mono<Long> result = followerService.quantityFollowers(1L);

        StepVerifier.create(result)
                .expectNext(1L)
                .verifyComplete();
        Mockito.verify(followerPersistencePort, times(1)).findAllByFollowerId(anyLong());
    }

    @Test
    @DisplayName("When Follower Information Is Correct Expect Followed Saved Successfully")
    void when_FollowerInformationIsCorrect_Expect_FollowerSavedSuccessfully() {
        Follower follower=TestUtilsFollower.buildFollowerMock();
        when(followerPersistencePort.save(any(Follower.class))).thenReturn(Mono.just(follower));

        Mono<Follower> result = followerService.save(follower);

        StepVerifier.create(result)
                .expectNext(follower)
                .verifyComplete();

        Mockito.verify(followerPersistencePort, times(1)).save(any(Follower.class));
    }

    @Test
    @DisplayName("When followerId and followedId are correct, expect follower to be unfollowed successfully")
    void when_FollowerIdAndFollowedIdAreCorrect_Expect_FollowerUnfollowedSuccessfully() {
        Long followerId = 1L;
        Long followedId = 2L;
        Follower follower = TestUtilsFollower.buildFollowerMock();
        follower.getFollowed().setId(followedId);

        when(followerPersistencePort.findAllByFollowerId(anyLong())).thenReturn(Flux.just(follower));
        when(followerPersistencePort.delete(anyString())).thenReturn(Mono.empty());

        Mono<Void> result = followerService.unfollow(followerId, followedId);

        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(followerPersistencePort, times(1)).findAllByFollowerId(anyLong());
        Mockito.verify(followerPersistencePort, times(1)).delete(anyString());
    }

    @Test
    @DisplayName("Expect FollowerNotFoundException When Follower Id Does Not Exist")
    void Expect_FollowerNotFoundException_when_FollowerIdDoesNotExist() {
        Long followerId = 1L;
        Long followedId = 2L;

        when(followerPersistencePort.findAllByFollowerId(anyLong())).thenReturn(Flux.empty());

        Mono<Void> result = followerService.unfollow(followerId, followedId);

        StepVerifier.create(result)
                .expectError(FollowerNotFoundException.class)
                .verify();

        Mockito.verify(followerPersistencePort, times(1)).findAllByFollowerId(anyLong());
        Mockito.verify(followerPersistencePort, times(0)).delete(anyString());
    }

    @Test
    @DisplayName("Expect FollowedNotFoundException When Followed Id Does Not Exist")
    void Expect_FollowedNotFoundException_When_FollowedIdDoesNotExist() {
        Long followerId = 1L;
        Long followedId = 2L;
        Follower follower = TestUtilsFollower.buildFollowerMock();
        follower.getFollowed().setId(3L); // Different followedId

        when(followerPersistencePort.findAllByFollowerId(anyLong())).thenReturn(Flux.just(follower));

        Mono<Void> result = followerService.unfollow(followerId, followedId);

        StepVerifier.create(result)
                .expectError(FollowedNotFoundException.class)
                .verify();

        Mockito.verify(followerPersistencePort, times(1)).findAllByFollowerId(anyLong());
        Mockito.verify(followerPersistencePort, times(0)).delete(anyString());
    }

}
