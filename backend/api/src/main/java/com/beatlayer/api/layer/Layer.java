package com.beatlayer.api.layer;

import com.beatlayer.api.auth.User;
import com.beatlayer.api.jam.Jam;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;
import java.math.BigDecimal;

@Entity
@Table(name = "layers")
public class Layer {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "jam_id", nullable = false)
  private Jam jam;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_layer_id")
  private Layer parentLayer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", nullable = false)
  private User createdBy;

  @Column(name = "s3_key_original", nullable = false)
  private String s3KeyOriginal;

  @Column(name = "s3_key_normalized")
  private String s3KeyNormalized;

  @Column(name = "duration_ms", nullable = false)
  private Integer durationMs;

  @Column(nullable = false)
  private Integer bars;

  @Column(name = "start_offset_ms", nullable = false)
  private Integer startOffsetMs = 0;

  @Column(name = "gain_db", nullable = false)
  private BigDecimal gainDb = BigDecimal.ZERO;

  @Column(nullable = false)
  private BigDecimal pan = BigDecimal.ZERO;

  @Column(name = "key_override")
  private String keyOverride;

  @Column(name = "bpm_override")
  private Integer bpmOverride;

  @Column
  private String instrument;

  @Column(columnDefinition = "text")
  private String notes;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @PrePersist
  public void prePersist() {
    if (id == null) {
      id = UUID.randomUUID();
    }
    if (createdAt == null) {
      createdAt = Instant.now();
    }
    if (startOffsetMs == null) {
      startOffsetMs = 0;
    }
    if (gainDb == null) {
      gainDb = BigDecimal.ZERO;
    }
    if (pan == null) {
      pan = BigDecimal.ZERO;
    }
  }

  // --- getters & setters ---

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Jam getJam() {
    return jam;
  }

  public void setJam(Jam jam) {
    this.jam = jam;
  }

  public Layer getParentLayer() {
    return parentLayer;
  }

  public void setParentLayer(Layer parentLayer) {
    this.parentLayer = parentLayer;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public String getS3KeyOriginal() {
    return s3KeyOriginal;
  }

  public void setS3KeyOriginal(String s3KeyOriginal) {
    this.s3KeyOriginal = s3KeyOriginal;
  }

  public String getS3KeyNormalized() {
    return s3KeyNormalized;
  }

  public void setS3KeyNormalized(String s3KeyNormalized) {
    this.s3KeyNormalized = s3KeyNormalized;
  }

  public Integer getDurationMs() {
    return durationMs;
  }

  public void setDurationMs(Integer durationMs) {
    this.durationMs = durationMs;
  }

  public Integer getBars() {
    return bars;
  }

  public void setBars(Integer bars) {
    this.bars = bars;
  }

  public Integer getStartOffsetMs() {
    return startOffsetMs;
  }

  public void setStartOffsetMs(Integer startOffsetMs) {
    this.startOffsetMs = startOffsetMs;
  }

  public BigDecimal getGainDb() {
    return gainDb;
  }

  public void setGainDb(BigDecimal gainDb) {
    this.gainDb = gainDb;
  }

  public BigDecimal getPan() {
    return pan;
  }

  public void setPan(BigDecimal pan) {
    this.pan = pan;
  }

  public String getKeyOverride() {
    return keyOverride;
  }

  public void setKeyOverride(String keyOverride) {
    this.keyOverride = keyOverride;
  }

  public Integer getBpmOverride() {
    return bpmOverride;
  }

  public void setBpmOverride(Integer bpmOverride) {
    this.bpmOverride = bpmOverride;
  }

  public String getInstrument() {
    return instrument;
  }

  public void setInstrument(String instrument) {
    this.instrument = instrument;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
