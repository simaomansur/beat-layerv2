package com.beatlayer.api.audio;

import java.util.UUID;

public record CreateRootAudioRequest(
        UUID createdByUserId,
        String storageLocator,
        String mimeType,
        Integer durationMs,
        Integer sampleRate,
        Integer channels,
        Long bytes,
        String instrument,
        String notes
) {}
