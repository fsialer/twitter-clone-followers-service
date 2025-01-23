package com.fernando.ms.followers.app.infrastructure.adapter.output.persistence;

import com.fernando.ms.followers.app.Utils.TestUtilsFollower;
import com.fernando.ms.followers.app.domain.models.Follower;
import com.fernando.ms.followers.app.infrastructure.adapter.output.persistence.mapper.FollowerPersistenceMapper;
import com.fernando.ms.followers.app.infrastructure.adapter.output.persistence.models.FollowerDocument;
import com.fernando.ms.followers.app.infrastructure.adapter.output.persistence.repository.FollowerReactiveMongoRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowerPersistenceAdapterTest {
    @Mock
    private FollowerReactiveMongoRepository followerReactiveMongoRepository;

    @Mock
    private FollowerPersistenceMapper followerPersistenceMapper;

    @InjectMocks
    private FollowerPersistenceAdapter followerPersistenceAdapter;

    @Test
    @DisplayName("When Follower Is Correct Expect A List Followed Exists")
    void When_FollowerIsCorrect_Expect_AListFollowedExists() {
        Follower follower= TestUtilsFollower.buildFollowerMock();
        FollowerDocument followerDocument=TestUtilsFollower.buildFollowerDocumentMock();
        when(followerReactiveMongoRepository.findAllByFollowerId(anyLong())).thenReturn(Flux.just(followerDocument));
        when(followerPersistenceMapper.toFollowers(any(Flux.class))).thenReturn(Flux.just(follower));

        Flux<Follower> result = followerPersistenceAdapter.findFollowers(1L);

        StepVerifier.create(result)
                .expectNext(follower)
                .verifyComplete();
        Mockito.verify(followerReactiveMongoRepository, times(1)).findAllByFollowerId(anyLong());
        Mockito.verify(followerPersistenceMapper, times(1)).toFollowers(any(Flux.class));
    }

    @Test
    @DisplayName("When Save Follower Expect Follower Saved Successfully")
    void when_SaveFollower_Expect_FollowerSavedSuccessfully() {
        FollowerDocument followerDocument=TestUtilsFollower.buildFollowerDocumentMock();
        Follower follower= TestUtilsFollower.buildFollowerMock();
        when(followerPersistenceMapper.toFollowerDocument(any(Follower.class))).thenReturn(followerDocument);
        when(followerReactiveMongoRepository.save(any(FollowerDocument.class))).thenReturn(Mono.just(followerDocument));
        when(followerPersistenceMapper.toFollower(any(Mono.class))).thenReturn(Mono.just(follower));

        Mono<Follower> result = followerPersistenceAdapter.save(follower);

        StepVerifier.create(result)
                .expectNext(follower)
                .verifyComplete();
        Mockito.verify(followerReactiveMongoRepository, times(1)).save(any(FollowerDocument.class));
        Mockito.verify(followerPersistenceMapper, times(1)).toFollowerDocument(any(Follower.class));
        Mockito.verify(followerPersistenceMapper, times(1)).toFollower(any(Mono.class));
    }

    @Test
    @DisplayName("When Delete Is Called With A Valid Id Expect Follower To Be Deleted Successfully")
    void when_DeleteIsCalledWithValidId_Expect_FollowerDeletedSuccessfully() {

        when(followerReactiveMongoRepository.deleteById(anyString())).thenReturn(Mono.empty());

        Mono<Void> result = followerPersistenceAdapter.delete("678962f84a0e146fa0f20569");

        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(followerReactiveMongoRepository, times(1)).deleteById(anyString());
    }

    @Test
    @DisplayName("When existsByFollowerIdFollowedId is called with valid followerId and followedId, expect true")
    void when_ExistsByFollowerIdFollowedIdIsCalledWithValidIds_Expect_True() {
        Long followerId = 1L;
        Long followedId = 2L;

        when(followerReactiveMongoRepository.existsByFollowerIdAndFollowedId(followerId, followedId)).thenReturn(Mono.just(true));

        Mono<Boolean> result = followerPersistenceAdapter.existsByFollowerIdFollowedId(followerId, followedId);

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        Mockito.verify(followerReactiveMongoRepository, times(1)).existsByFollowerIdAndFollowedId(followerId, followedId);
    }

    @Test
    @DisplayName("When existsByFollowerIdFollowedId is called with invalid followerId and followedId, expect false")
    void when_ExistsByFollowerIdFollowedIdIsCalledWithInvalidIds_Expect_False() {
        Long followerId = 1L;
        Long followedId = 2L;

        when(followerReactiveMongoRepository.existsByFollowerIdAndFollowedId(followerId, followedId)).thenReturn(Mono.just(false));

        Mono<Boolean> result = followerPersistenceAdapter.existsByFollowerIdFollowedId(followerId, followedId);

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        Mockito.verify(followerReactiveMongoRepository, times(1)).existsByFollowerIdAndFollowedId(followerId, followedId);
    }
}
