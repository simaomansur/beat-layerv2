package com.beatlayer.api.jam;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public class JamDtos {

  public record JamResponse(
      UUID id,
      String title,
      String key,            // maps from Jam.musicalKey
      Integer bpm,
      String genre,
      String instrumentHint,
      String baseAudioUrl,
      boolean isPremium,
      Integer layerCreditCost,
      boolean isContest,
      boolean isLocked,
      Instant createdAt
  ) {}

  // This matches what your frontend already sends:
  // title, key, bpm, genre, instrumentHint.
  public record CreateJamRequest(
      @NotBlank String title,
      @NotBlank String key,
      @NotNull @Min(40) @Max(300) Integer bpm,
      String genre,
      String instrumentHint,

      // optional extras (can be null; safe with current frontend)
      Boolean isPremium,
      Integer layerCreditCost,
      Boolean isContest,
      String contestDescription
  ) {}

  public record UpdateJamRequest(
      String title,
      String key,
      Integer bpm,
      String genre,
      String instrumentHint,

      Boolean isPremium,
      Integer layerCreditCost,
      Boolean isContest,
      Boolean isLocked,
      String contestDescription
  ) {}

  public static JamResponse fromEntity(Jam j) {
    return new JamResponse(
        j.getId(),
        j.getTitle(),
        j.getMusicalKey(),      // 👈 map musicalKey → "key" in JSON
        j.getBpm(),
        j.getGenre(),
        j.getInstrumentHint(),
        j.getBaseAudioUrl(),
        j.isPremium(),
        j.getLayerCreditCost(),
        j.isContest(),
        j.isLocked(),
        j.getCreatedAt()
    );
  }
}
