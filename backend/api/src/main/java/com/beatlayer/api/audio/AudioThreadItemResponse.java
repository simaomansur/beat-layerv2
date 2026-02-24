package com.beatlayer.api.audio;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AudioThreadItemResponse(
        UUID jamId,
        UUID threadItemId,
        UUID audioAssetId,
        UUID createdByUserId,
        String itemType,
        UUID parentItemId,
        String storageLocator,
        String mimeType,
        Integer durationMs,
        OffsetDateTime createdAt
) {}
