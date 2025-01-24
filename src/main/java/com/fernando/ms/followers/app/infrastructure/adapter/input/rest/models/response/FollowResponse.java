package com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.response;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowResponse {
    private String id;
    private Long follower;
    private Long followed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowResponse that = (FollowResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(follower, that.follower)&&
                Objects.equals(followed, that.followed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, follower,followed);
    }
}
