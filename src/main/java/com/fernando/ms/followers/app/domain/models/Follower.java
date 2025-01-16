package com.fernando.ms.followers.app.domain.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Follower {
    private String id;
    private Long followerId;
    private Long followedId;
}
