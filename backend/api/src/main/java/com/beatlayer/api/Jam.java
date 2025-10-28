package com.beatlayer.api;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "jams")
public class Jam {
  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false)
  private String title;

  @Column(name = "\"key\"", nullable = false)
  private String key;

  @Column(nullable = false)
  private int bpm;

  private String genre;

  @Column(name = "instrument_hint")
  private String instrumentHint;

  @Column(name = "created_by", nullable = false)
  private UUID createdBy;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt = Instant.now();

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt = Instant.now();

  @PrePersist
  @PreUpdate
  void updateTimestamp() {
    updatedAt = Instant.now();
  }

  // Getters and setters
  public UUID getId() { return id; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public String getKey() { return key; }
  public void setKey(String key) { this.key = key; }
  public int getBpm() { return bpm; }
  public void setBpm(int bpm) { this.bpm = bpm; }
  public String getGenre() { return genre; }
  public void setGenre(String genre) { this.genre = genre; }
  public String getInstrumentHint() { return instrumentHint; }
  public void setInstrumentHint(String instrumentHint) { this.instrumentHint = instrumentHint; }
  public UUID getCreatedBy() { return createdBy; }
  public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }
  public Instant getCreatedAt() { return createdAt; }
  public Instant getUpdatedAt() { return updatedAt; }
}
