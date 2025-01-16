package com.fernando.ms.followers.app.application.services;

import com.fernando.ms.followers.app.Utils.TestUtilsFollower;
import com.fernando.ms.followers.app.application.ports.output.FollowerPersistencePort;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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

}
