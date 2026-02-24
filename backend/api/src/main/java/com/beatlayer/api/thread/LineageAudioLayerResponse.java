package com.beatlayer.api.thread;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record LineageAudioLayerResponse(
        UUID threadItemId,
        UUID jamId,
        UUID parentItemId,
        UUID createdByUserId,
        OffsetDateTime createdAt,

        UUID audioAssetId,
        String storageLocator,
        String mimeType,
        Integer durationMs,

        Integer startOffsetMs,
        Integer trimStartMs,
        Integer trimEndMs,
        BigDecimal gainDb,
        BigDecimal pan,
        Boolean muted,

        String instrument,
        String notes
) {}
