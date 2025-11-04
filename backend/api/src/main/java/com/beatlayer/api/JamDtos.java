package com.beatlayer.api;

import jakarta.validation.constraints.*;

import java.util.UUID;

public class JamDtos {

  public record CreateJamRequest(
      @NotBlank String title,
      // allow things like C, C#, Db, Am, F#m, etc. (loose for now)
      @NotBlank @Pattern(regexp = "^[A-G](#|b)?m?$") String key,
      @Min(40) @Max(240) int bpm,
      String genre,
      String instrumentHint
  ) {}

  public record JamResponse(
      UUID id,
      String title,
      String key,
      int bpm,
      String genre,
      String instrumentHint
  ) {}

  public record UpdateJamRequest(
      @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
      String title,

      String key,

      @Min(value = 40, message = "BPM must be at least 40")
      @Max(value = 300, message = "BPM must be at most 300")
      Integer bpm,

      String genre,
      String instrumentHint
  ) {}

  public static JamResponse fromEntity(Jam j) {
    return new JamResponse(
        j.getId(),
        j.getTitle(),
        j.getKey(),          // <-- now a String
        j.getBpm(),
        j.getGenre(),
        j.getInstrumentHint()
    );
  }
}
