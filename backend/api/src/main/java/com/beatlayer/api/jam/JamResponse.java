package com.beatlayer.api.jam;

import java.time.OffsetDateTime;
import java.util.UUID;

public record JamResponse(
        UUID id,
        UUID createdByUserId,
        String title,
        String description,
        Integer loopLengthMs,
        Integer bpm,
        String musicalKey,
        String genre,
        String instrumentHint,
        String visibility,
        UUID rootItemId,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static JamResponse from(Jam j) {
        return new JamResponse(
                j.getId(),
                j.getCreatedBy().getId(),
                j.getTitle(),
                j.getDescription(),
                j.getLoopLengthMs(),
                j.getBpm(),
                j.getMusicalKey(),
                j.getGenre(),
                j.getInstrumentHint(),
                j.getVisibility(),
                j.getRootItem() == null ? null : j.getRootItem().getId(),
                j.getCreatedAt(),
                j.getUpdatedAt()
        );
    }
}
