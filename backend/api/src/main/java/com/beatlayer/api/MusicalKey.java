package com.beatlayer.api;

public enum MusicalKey {
  C, C_SHARP, D, D_SHARP, E, F, F_SHARP, G, G_SHARP, A, A_SHARP, B,
  Cm, C_SHARPm, Dm, D_SHARPm, Em, Fm, F_SHARPm, Gm, G_SHARPm, Am, A_SHARPm, Bm;

  // Convert to DB enum labels (C#, D# etc.)
  public String toDb() {
    return switch (this) {
      case C_SHARP -> "C#";
      case D_SHARP -> "D#";
      case F_SHARP -> "F#";
      case G_SHARP -> "G#";
      case A_SHARP -> "A#";
      case C_SHARPm -> "C#m";
      case D_SHARPm -> "D#m";
      case F_SHARPm -> "F#m";
      case G_SHARPm -> "G#m";
      case A_SHARPm -> "A#m";
      default -> this.name();
    };
  }

  public static MusicalKey fromDb(String s) {
    return switch (s) {
      case "C#" -> C_SHARP; case "D#" -> D_SHARP; case "F#" -> F_SHARP;
      case "G#" -> G_SHARP; case "A#" -> A_SHARP;
      case "C#m" -> C_SHARPm; case "D#m" -> D_SHARPm; case "F#m" -> F_SHARPm;
      case "G#m" -> G_SHARPm; case "A#m" -> A_SHARPm;
      default -> MusicalKey.valueOf(s);
    };
  }
}
