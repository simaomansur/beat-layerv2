package com.beatlayer.api.user;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String handle,
        String email,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static UserResponse from(User u) {
        return new UserResponse(
                u.getId(),
                u.getHandle(),
                u.getEmail(),
                u.getCreatedAt(),
                u.getUpdatedAt()
        );
    }
}
