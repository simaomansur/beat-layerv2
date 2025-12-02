package com.beatlayer.api.jam;

import com.beatlayer.api.auth.User;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "jams")
public class Jam {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @Column(nullable = false, length = 255)
  private String title;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", nullable = false)
  private User createdBy;

  // musical_key in the database
  @Column(name = "musical_key", nullable = false)
  private String musicalKey;

  @Column(nullable = false)
  private Integer bpm;

  @Column
  private String genre;

  @Column(name = "instrument_hint")
  private String instrumentHint;

  @Column(name = "base_audio_url")
  private String baseAudioUrl;

  // premium / credits
  @Column(name = "is_premium", nullable = false)
  private boolean premium = false;

  @Column(name = "layer_credit_cost", nullable = false)
  private Integer layerCreditCost = 1;

  // contest fields
  @Column(name = "is_contest", nullable = false)
  private boolean contest = false;

  @Column(name = "contest_ends_at")
  private Instant contestEndsAt;

  @Column(name = "contest_description")
  private String contestDescription;

  @Column(name = "is_locked", nullable = false)
  private boolean locked = false;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @PrePersist
  public void prePersist() {
    Instant now = Instant.now();

    if (id == null) {
      id = UUID.randomUUID();
    }
    if (createdAt == null) {
      createdAt = now;
    }
    if (updatedAt == null) {
      updatedAt = now;
    }
    // enforce sane defaults in case something upstream forgets
    if (layerCreditCost == null || layerCreditCost <= 0) {
      layerCreditCost = 1;
    }
  }

  @PreUpdate
  public void preUpdate() {
    updatedAt = Instant.now();
    if (layerCreditCost == null || layerCreditCost <= 0) {
      layerCreditCost = 1;
    }
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

  public String getMusicalKey() {
    return musicalKey;
  }

  public void setMusicalKey(String musicalKey) {
    this.musicalKey = musicalKey;
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

  public String getBaseAudioUrl() {
    return baseAudioUrl;
  }

  public void setBaseAudioUrl(String baseAudioUrl) {
    this.baseAudioUrl = baseAudioUrl;
  }

  public boolean isPremium() {
    return premium;
  }

  public void setPremium(boolean premium) {
    this.premium = premium;
  }

  public Integer getLayerCreditCost() {
    return layerCreditCost;
  }

  public void setLayerCreditCost(Integer layerCreditCost) {
    this.layerCreditCost = layerCreditCost;
  }

  public boolean isContest() {
    return contest;
  }

  public void setContest(boolean contest) {
    this.contest = contest;
  }

  public Instant getContestEndsAt() {
    return contestEndsAt;
  }

  public void setContestEndsAt(Instant contestEndsAt) {
    this.contestEndsAt = contestEndsAt;
  }

  public String getContestDescription() {
    return contestDescription;
  }

  public void setContestDescription(String contestDescription) {
    this.contestDescription = contestDescription;
  }

  public boolean isLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
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
