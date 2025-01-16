package com.fernando.ms.followers.app.infrastructure.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCatalog {
    FOLLOWER_BAD_PARAMETERS("FOLLOWER_MS_001", "Invalid parameters for creation follower"),
    INTERNAL_SERVER_ERROR("FOLLOWER_MS_000", "Internal server error.");
    private final String code;
    private final String message;
}
