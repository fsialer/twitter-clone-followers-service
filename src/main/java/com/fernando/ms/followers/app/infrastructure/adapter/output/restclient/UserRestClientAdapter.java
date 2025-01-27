package com.fernando.ms.followers.app.infrastructure.adapter.output.restclient;

import com.fernando.ms.followers.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.followers.app.domain.models.User;
import com.fernando.ms.followers.app.infrastructure.adapter.output.restclient.client.UserWebClient;
import com.fernando.ms.followers.app.infrastructure.adapter.output.restclient.mapper.UserRestClientMapper;
import com.fernando.ms.followers.app.infrastructure.adapter.output.restclient.models.response.ExistsUserResponse;
import com.fernando.ms.followers.app.infrastructure.adapter.output.restclient.models.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserRestClientAdapter implements ExternalUserOutputPort {
    private final UserWebClient userWebClient;
    private final UserRestClientMapper userRestClientMapper;
    @Override
    public Mono<Boolean> verify(Long id) {
        return userWebClient.verify(id);
    }

    @Override
    public Flux<User> findByIds(List<Long> ids) {
        return userWebClient.findByIds(ids)
                .flatMap(user->{
                    return Flux.just(userRestClientMapper.toUser(user));
                });
    }

    @Override
    public Mono<User> findById(Long id) {
        return userWebClient.findById(id)
                .map(userRestClientMapper::toUser);
    }


}
