package com.beatlayer.api.jam;

import java.time.Instant;
import java.util.UUID;

public class JamDtos {

  public record JamResponse(
      UUID id,
      String title,
      String key,            // API name stays "key"
      Integer bpm,
      String genre,
      String instrumentHint,
      String baseAudioUrl,
      Instant createdAt,
      Instant updatedAt
  ) {}

  public record CreateJamRequest(
      String title,
      String key,
      Integer bpm,
      String genre,
      String instrumentHint,
      String baseAudioUrl
  ) {}

  public record UpdateJamRequest(
      String title,
      String key,
      Integer bpm,
      String genre,
      String instrumentHint,
      String baseAudioUrl
  ) {}

  public static JamResponse fromEntity(Jam j) {
    return new JamResponse(
        j.getId(),
        j.getTitle(),
        j.getMusicalKey(),      // map entity -> API
        j.getBpm(),
        j.getGenre(),
        j.getInstrumentHint(),
        j.getBaseAudioUrl(),
        j.getCreatedAt(),
        j.getUpdatedAt()
    );
  }

  public static void applyUpdate(Jam j, UpdateJamRequest req) {
    if (req.title() != null) {
      j.setTitle(req.title());
    }
    if (req.key() != null) {
      j.setMusicalKey(req.key());
    }
    if (req.bpm() != null) {
      j.setBpm(req.bpm());
    }
    if (req.genre() != null) {
      j.setGenre(req.genre());
    }
    if (req.instrumentHint() != null) {
      j.setInstrumentHint(req.instrumentHint());
    }
    if (req.baseAudioUrl() != null) {
      j.setBaseAudioUrl(req.baseAudioUrl());
    }
  }
}
