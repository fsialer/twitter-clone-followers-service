package com.fernando.ms.followers.app.application.services;

import com.fernando.ms.followers.app.Utils.TestUtilsFollower;
import com.fernando.ms.followers.app.Utils.TestUtilsUser;
import com.fernando.ms.followers.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.followers.app.application.ports.output.FollowerPersistencePort;
import com.fernando.ms.followers.app.domain.exception.FollowedNotFoundException;
import com.fernando.ms.followers.app.domain.exception.FollowerNotFoundException;
import com.fernando.ms.followers.app.domain.exception.FollowerRuleException;
import com.fernando.ms.followers.app.domain.models.Follower;
import com.fernando.ms.followers.app.domain.models.User;
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

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowerServiceTest {
    @Mock
    private FollowerPersistencePort followerPersistencePort;

    @Mock
    private ExternalUserOutputPort externalUserOutputPort;

    @InjectMocks
    private FollowerService followerService;

    @Test
    @DisplayName("When followerId And TargetType Are Correct Expect Quantity Followers Exists")
    void When_TargetIDAndTargetTypeAreCorrect_Expect_QuantityLikeExists() {
        Follower follower = TestUtilsFollower.buildFollowerMock();

        when(followerPersistencePort.findFollowers(anyLong())).thenReturn(Flux.just(follower));

        Mono<Long> result = followerService.quantityFollowers(1L);

        StepVerifier.create(result)
                .expectNext(1L)
                .verifyComplete();
        Mockito.verify(followerPersistencePort, times(1)).findFollowers(anyLong());
    }

    @Test
    @DisplayName("When follower information is correct, expect follower to be saved successfully")
    void when_FollowerInformationIsCorrect_Expect_FollowerSavedSuccessfully() {
        Follower follower = TestUtilsFollower.buildFollowerMock();

        when(followerPersistencePort.existsByFollowerIdFollowedId(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(externalUserOutputPort.verify(anyLong())).thenReturn(Mono.just(true));
        when(followerPersistencePort.save(any(Follower.class))).thenReturn(Mono.just(follower));

        Mono<Follower> result = followerService.save(follower);

        StepVerifier.create(result)
                .expectNext(follower)
                .verifyComplete();

        Mockito.verify(followerPersistencePort, times(1)).existsByFollowerIdFollowedId(anyLong(), anyLong());
        Mockito.verify(externalUserOutputPort, times(2)).verify(anyLong());
        Mockito.verify(followerPersistencePort, times(1)).save(any(Follower.class));
    }

    @Test
    @DisplayName("When follower already exists, expect FollowerRuleException")
    void when_FollowerAlreadyExists_Expect_FollowerRuleException() {
        Follower follower = TestUtilsFollower.buildFollowerMock();

        when(followerPersistencePort.existsByFollowerIdFollowedId(anyLong(), anyLong())).thenReturn(Mono.just(true));

        Mono<Follower> result = followerService.save(follower);

        StepVerifier.create(result)
                .expectError(FollowerRuleException.class)
                .verify();

        Mockito.verify(followerPersistencePort, times(1)).existsByFollowerIdFollowedId(anyLong(), anyLong());
        Mockito.verify(externalUserOutputPort, times(0)).verify(anyLong());
        Mockito.verify(followerPersistencePort, times(0)).save(any(Follower.class));
    }

    @Test
    @DisplayName("When follower does not exist, expect FollowerNotFoundException")
    void when_FollowerDoesNotExist_Expect_FollowerNotFoundException() {
        Follower follower = TestUtilsFollower.buildFollowerMock();

        when(followerPersistencePort.existsByFollowerIdFollowedId(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(externalUserOutputPort.verify(follower.getFollower().getId())).thenReturn(Mono.just(false));

        Mono<Follower> result = followerService.save(follower);

        StepVerifier.create(result)
                .expectError(FollowerNotFoundException.class)
                .verify();

        Mockito.verify(followerPersistencePort, times(1)).existsByFollowerIdFollowedId(anyLong(), anyLong());
        Mockito.verify(externalUserOutputPort, times(1)).verify(follower.getFollower().getId());
        Mockito.verify(followerPersistencePort, times(0)).save(any(Follower.class));
    }

    @Test
    @DisplayName("When followed does not exist, expect FollowedNotFoundException")
    void when_FollowedDoesNotExist_Expect_FollowedNotFoundException() {
        Follower follower = TestUtilsFollower.buildFollowerMock();

        when(followerPersistencePort.existsByFollowerIdFollowedId(anyLong(), anyLong())).thenReturn(Mono.just(false));
        when(externalUserOutputPort.verify(follower.getFollower().getId())).thenReturn(Mono.just(true));
        when(externalUserOutputPort.verify(follower.getFollowed().getId())).thenReturn(Mono.just(false));

        Mono<Follower> result = followerService.save(follower);

        StepVerifier.create(result)
                .expectError(FollowedNotFoundException.class)
                .verify();

        Mockito.verify(followerPersistencePort, times(1)).existsByFollowerIdFollowedId(anyLong(), anyLong());
        Mockito.verify(externalUserOutputPort, times(1)).verify(follower.getFollower().getId());
        Mockito.verify(externalUserOutputPort, times(1)).verify(follower.getFollowed().getId());
        Mockito.verify(followerPersistencePort, times(0)).save(any(Follower.class));
    }

    @Test
    @DisplayName("When followerId and followedId are correct, expect follower to be unfollowed successfully")
    void when_FollowerIdAndFollowedIdAreCorrect_Expect_FollowerUnfollowedSuccessfully() {
        Long followerId = 1L;
        Long followedId = 2L;
        Follower follower = TestUtilsFollower.buildFollowerMock();
        follower.getFollowed().setId(followedId);

        when(followerPersistencePort.findFollowers(anyLong())).thenReturn(Flux.just(follower));
        when(followerPersistencePort.delete(anyString())).thenReturn(Mono.empty());

        Mono<Void> result = followerService.unfollow(followerId, followedId);

        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(followerPersistencePort, times(1)).findFollowers(anyLong());
        Mockito.verify(followerPersistencePort, times(1)).delete(anyString());
    }

    @Test
    @DisplayName("Expect FollowerNotFoundException When Follower Id Does Not Exist")
    void Expect_FollowerNotFoundException_when_FollowerIdDoesNotExist() {
        Long followerId = 1L;
        Long followedId = 2L;

        when(followerPersistencePort.findFollowers(anyLong())).thenReturn(Flux.empty());

        Mono<Void> result = followerService.unfollow(followerId, followedId);

        StepVerifier.create(result)
                .expectError(FollowerNotFoundException.class)
                .verify();

        Mockito.verify(followerPersistencePort, times(1)).findFollowers(anyLong());
        Mockito.verify(followerPersistencePort, times(0)).delete(anyString());
    }

    @Test
    @DisplayName("Expect FollowedNotFoundException When Followed Id Does Not Exist")
    void Expect_FollowedNotFoundException_When_FollowedIdDoesNotExist() {
        Long followerId = 1L;
        Long followedId = 2L;
        Follower follower = TestUtilsFollower.buildFollowerMock();
        follower.getFollowed().setId(3L); // Different followedId

        when(followerPersistencePort.findFollowers(anyLong())).thenReturn(Flux.just(follower));

        Mono<Void> result = followerService.unfollow(followerId, followedId);

        StepVerifier.create(result)
                .expectError(FollowedNotFoundException.class)
                .verify();

        Mockito.verify(followerPersistencePort, times(1)).findFollowers(anyLong());
        Mockito.verify(followerPersistencePort, times(0)).delete(anyString());
    }

    @Test
    @DisplayName("When findFollowersPaginated is called with valid followerId, page, and size, expect a list of users")
    void When_FindFollowersPaginatedIsCalledWithValidParams_Expect_ListOfUsers() {
        Long followerId = 1L;
        Long page = 0L;
        Long size = 10L;
        Follower follower = TestUtilsFollower.buildFollowerMock();
        User user = TestUtilsUser.buildUserMock();

        when(followerPersistencePort.findFollowersPaginated(followerId, page, size)).thenReturn(Flux.just(follower));
        when(externalUserOutputPort.findByIds(Collections.singletonList(follower.getFollower().getId()))).thenReturn(Flux.just(user));

        Flux<User> result = followerService.findFollowersPaginated(followerId, page, size);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();

        Mockito.verify(followerPersistencePort, times(1)).findFollowersPaginated(followerId, page, size);
        Mockito.verify(externalUserOutputPort, times(1)).findByIds(Collections.singletonList(follower.getFollower().getId()));
    }

    @Test
    @DisplayName("When findAllFollowedByFollower is called with valid followedId, expect a list of followers")
    void when_FindAllFollowedByFollowerIsCalledWithValidFollowedId_Expect_ListOfFollowers() {
        Long followedId = 1L;
        Follower follower = TestUtilsFollower.buildFollowerMock();

        when(followerPersistencePort.findAllFollowedByFollowerPaginated(followedId)).thenReturn(Flux.just(follower));

        Flux<Follower> result = followerService.findAllFollowedByFollower(followedId);

        StepVerifier.create(result)
                .expectNext(follower)
                .verifyComplete();

        Mockito.verify(followerPersistencePort, times(1)).findAllFollowedByFollowerPaginated(followedId);
    }

    @Test
    @DisplayName("When follower identifier is valid, expect a list of users")
    void when_FollowerIdentifierIsValid_Expect_AListOfUsers() {
        Long followerId = 1L;
        Follower follower = TestUtilsFollower.buildFollowerMock();
        User user = TestUtilsUser.buildUserMock();

        when(followerPersistencePort.findFollowers(anyLong())).thenReturn(Flux.just(follower));
        when(externalUserOutputPort.findByIds(anyList())).thenReturn(Flux.just(user));

        Flux<User> result = followerService.findFollowers(followerId);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();

        Mockito.verify(followerPersistencePort, times(1)).findFollowers(anyLong());
        Mockito.verify(externalUserOutputPort, times(1)).findByIds(anyList());
    }
}
