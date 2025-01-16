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
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
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

        Flux<Follower> result = followerPersistenceAdapter.findAllByFollowerId(1L);

        StepVerifier.create(result)
                .expectNext(follower)
                .verifyComplete();
        Mockito.verify(followerReactiveMongoRepository, times(1)).findAllByFollowerId(anyLong());
        Mockito.verify(followerPersistenceMapper, times(1)).toFollowers(any(Flux.class));
    }
}
