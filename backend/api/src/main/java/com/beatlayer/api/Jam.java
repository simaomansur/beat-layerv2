package com.beatlayer.api;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "jams")
public class Jam {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @Column(nullable = false)
  private String title;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", nullable = false)
  private User createdBy;

  // "key" is a reserved-ish word in SQL, but fine in Java;
  // we map it explicitly to the quoted column.
  @Column(name = "\"key\"", nullable = false)
  private String key;

  @Column(nullable = false)
  private Integer bpm;

  @Column
  private String genre;

  @Column(name = "instrument_hint")
  private String instrumentHint;

  // 👇 NEW: where the base layer audio file is accessible from (e.g. /audio/xyz.webm)
  @Column(name = "base_audio_url")
  private String baseAudioUrl;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @PrePersist
  public void prePersist() {
    if (id == null) {
      id = UUID.randomUUID();
    }
    Instant now = Instant.now();
    if (createdAt == null) {
      createdAt = now;
    }
    if (updatedAt == null) {
      updatedAt = now;
    }
  }

  @PreUpdate
  public void preUpdate() {
    updatedAt = Instant.now();
  }

  // Getters and setters

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Integer getBpm() {
    return bpm;
  }

  public void setBpm(Integer bpm) {
    this.bpm = bpm;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public String getInstrumentHint() {
    return instrumentHint;
  }

  public void setInstrumentHint(String instrumentHint) {
    this.instrumentHint = instrumentHint;
  }

  // 👇 NEW getter/setter
  public String getBaseAudioUrl() {
    return baseAudioUrl;
  }

  public void setBaseAudioUrl(String baseAudioUrl) {
    this.baseAudioUrl = baseAudioUrl;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }
}
