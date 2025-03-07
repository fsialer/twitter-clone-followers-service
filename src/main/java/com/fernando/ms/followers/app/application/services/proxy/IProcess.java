package com.fernando.ms.followers.app.application.services.proxy;

import com.fernando.ms.followers.app.domain.models.Follower;
import reactor.core.publisher.Mono;

public interface IProcess {
    Mono<Follower> doProcess(Follower follower);
}
