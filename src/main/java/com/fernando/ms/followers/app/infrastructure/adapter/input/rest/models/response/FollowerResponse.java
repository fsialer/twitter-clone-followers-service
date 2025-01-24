package com.fernando.ms.followers.app.infrastructure.adapter.input.rest.models.response;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowerResponse {
    private Long id;
    private String names;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowerResponse that = (FollowerResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(names, that.names);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, names);
    }
}
