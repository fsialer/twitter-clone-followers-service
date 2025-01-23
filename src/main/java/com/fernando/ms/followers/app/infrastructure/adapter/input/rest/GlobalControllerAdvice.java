package com.fernando.ms.followers.app.infrastructure.adapter.input.rest;

import com.fernando.ms.followers.app.domain.exception.FollowedNotFoundException;
import com.fernando.ms.followers.app.domain.exception.FollowerNotFoundException;
import com.fernando.ms.followers.app.domain.exception.FollowerRuleException;
import com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.response.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;

import static com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.enums.ErrorType.FUNCTIONAL;
import static com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.enums.ErrorType.SYSTEM;
import static com.fernando.ms.followers.app.infrastructure.utils.ErrorCatalog.*;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ErrorResponse> handleWebExchangeBindException(
            WebExchangeBindException e) {
        BindingResult bindingResult = e.getBindingResult();
        return Mono.just(ErrorResponse.builder()
                .code(FOLLOWER_BAD_PARAMETERS.getCode())
                .type(FUNCTIONAL)
                .message(FOLLOWER_BAD_PARAMETERS.getMessage())
                .details(bindingResult.getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList())
                .timestamp(LocalDate.now().toString())
                .build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FollowerNotFoundException.class)
    public Mono<ErrorResponse> handleFollowerNotFoundException(
            FollowerNotFoundException e) {

        return Mono.just(ErrorResponse.builder()
                .code(FOLLOWER_NOT_FOUND.getCode())
                .type(FUNCTIONAL)
                .message(FOLLOWER_NOT_FOUND.getMessage())
                .timestamp(LocalDate.now().toString())
                .build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FollowedNotFoundException.class)
    public Mono<ErrorResponse> handleFollowedNotFoundException(
            FollowedNotFoundException e) {

        return Mono.just(ErrorResponse.builder()
                .code(FOLLOWED_NOT_FOUND.getCode())
                .type(FUNCTIONAL)
                .message(FOLLOWED_NOT_FOUND.getMessage())
                .timestamp(LocalDate.now().toString())
                .build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FollowerRuleException.class)
    public Mono<ErrorResponse> handleFollowerRuleException(
            FollowerRuleException e) {
        return Mono.just(ErrorResponse.builder()
                .code(FOLLOWER_RULE.getCode())
                .type(FUNCTIONAL)
                .message(FOLLOWER_RULE.getMessage())
                        .details(Collections.singletonList(e.getMessage()))
                .timestamp(LocalDate.now().toString())
                .build());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Mono<ErrorResponse> handleException(Exception e) {

        return Mono.just(ErrorResponse.builder()
                .code(INTERNAL_SERVER_ERROR.getCode())
                .type(SYSTEM)
                .message(INTERNAL_SERVER_ERROR.getMessage())
                .details(Collections.singletonList(e.getMessage()))
                .timestamp(LocalDate.now().toString())
                .build());
    }
}
