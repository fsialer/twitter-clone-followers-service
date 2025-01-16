package com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowerResponse {
    private String id;
    private Long follower;
    private Long followed;
}
