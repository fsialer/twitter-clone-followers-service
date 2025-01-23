package com.fernando.ms.followers.app.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.followers.app.infrastructure.adapter.output.persistence.models.FollowerDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FollowerReactiveMongoRepository extends ReactiveMongoRepository<FollowerDocument,String> ,FollowerReactiveMongoRepositoryCustom{
    Flux<FollowerDocument> findAllByFollowedId(Long followerId);
    Mono<FollowerDocument> findByFollowedId(String followed);
    Mono<Boolean> existsByFollowerIdAndFollowedId(Long followerId, Long followedId);
}
