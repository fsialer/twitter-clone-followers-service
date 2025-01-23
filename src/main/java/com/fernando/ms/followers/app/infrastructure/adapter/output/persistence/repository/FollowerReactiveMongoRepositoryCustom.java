package com.fernando.ms.followers.app.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.followers.app.infrastructure.adapter.output.persistence.models.FollowerDocument;
import reactor.core.publisher.Flux;

public interface FollowerReactiveMongoRepositoryCustom {
    Flux<FollowerDocument> findFollowersPaginated(Long followerId, Long page, Long size);
}
