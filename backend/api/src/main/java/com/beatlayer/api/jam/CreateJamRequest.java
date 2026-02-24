package com.beatlayer.api.jam;

import java.util.UUID;

public record CreateJamRequest(
        UUID createdByUserId,
        String title,
        String description,
        Integer loopLengthMs,
        Integer bpm,
        String musicalKey,
        String genre,
        String instrumentHint,
        String visibility
) {}
