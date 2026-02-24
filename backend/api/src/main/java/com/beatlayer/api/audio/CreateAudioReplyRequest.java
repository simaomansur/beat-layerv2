package com.beatlayer.api.audio;

import java.util.UUID;

public record CreateAudioReplyRequest(
        UUID createdByUserId,
        String storageLocator,
        String mimeType,
        Integer durationMs,
        Integer sampleRate,
        Integer channels,
        Long bytes,
        Integer startOffsetMs,
        Integer trimStartMs,
        Integer trimEndMs,
        String instrument,
        String notes
) {}
