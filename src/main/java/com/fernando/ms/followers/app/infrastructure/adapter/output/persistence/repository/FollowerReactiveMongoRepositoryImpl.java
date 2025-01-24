package com.fernando.ms.followers.app.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.followers.app.infrastructure.adapter.output.persistence.models.FollowerDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class FollowerReactiveMongoRepositoryImpl implements FollowerReactiveMongoRepositoryCustom{
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<FollowerDocument> findFollowersPaginated(Long followerId, Long page, Long size) {
        Query query = new Query()
                .addCriteria(Criteria.where("followedId").is(followerId)) // Filtrar por autor
                .skip((long) page * size)                            // Paginación: saltar los resultados
                .limit(Math.toIntExact(size));                                        // Limitar los resultados
        return mongoTemplate.find(query, FollowerDocument.class);
    }
}
