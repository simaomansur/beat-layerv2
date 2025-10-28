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
