package com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateFollowerRequest {
    @NotNull(message = "Field followedId cannot be null")
    private Long followedId;
}
