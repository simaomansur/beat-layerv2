package com.beatlayer.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public class JamDtos {

  public record CreateJamRequest(
      @NotBlank String title,
      @NotBlank String key,
      @NotNull @Min(40) @Max(240) Integer bpm,
      String genre,
      String instrumentHint
  ) {}

  // Partial update: all nullable, we only apply non-null fields
  public record UpdateJamRequest(
      String title,
      String key,
      Integer bpm,
      String genre,
      String instrumentHint
  ) {}

  public record JamResponse(
      UUID id,
      String title,
      String key,
      Integer bpm,
      String genre,
      String instrumentHint,
      Instant createdAt,
      UUID createdById,
      String createdByUsername,
      String baseAudioUrl
  ) {}

  public static JamResponse fromEntity(Jam j) {
    User u = j.getCreatedBy(); // should never be null for a valid Jam
    return new JamResponse(
        j.getId(),
        j.getTitle(),
        j.getKey(),
        j.getBpm(),
        j.getGenre(),
        j.getInstrumentHint(),
        j.getCreatedAt(),
        u != null ? u.getId() : null,
        u != null ? u.getHandle() : null,  // or getHandle() if that’s your field
        j.getBaseAudioUrl()
    );
  }
}
