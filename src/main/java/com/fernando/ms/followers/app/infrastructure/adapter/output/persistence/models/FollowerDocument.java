package com.fernando.ms.followers.app.infrastructure.adapter.output.persistence.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "followers")
public class FollowerDocument {
    @Id
    private String id;
    private Long followerId;
    private Long followedId;
    private LocalDateTime createAt;
}
