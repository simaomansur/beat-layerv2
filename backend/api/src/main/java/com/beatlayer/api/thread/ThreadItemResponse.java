package com.beatlayer.api.thread;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ThreadItemResponse(
        UUID id,
        UUID jamId,
        UUID parentItemId,
        UUID createdByUserId,
        String itemType,
        String body,
        OffsetDateTime createdAt
) {
    public static ThreadItemResponse from(ThreadItem t) {
        return new ThreadItemResponse(
                t.getId(),
                t.getJam().getId(),
                t.getParent() == null ? null : t.getParent().getId(),
                t.getCreatedBy().getId(),
                t.getItemType(),
                t.getBody(),
                t.getCreatedAt()
        );
    }
}
