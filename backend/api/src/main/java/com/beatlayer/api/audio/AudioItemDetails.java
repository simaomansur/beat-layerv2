package com.beatlayer.api.audio;

import com.beatlayer.api.thread.ThreadItem;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.math.BigDecimal;

@Entity
@Table(name = "audio_item_details")
public class AudioItemDetails {

    @Id
    private UUID threadItemId;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "thread_item_id")
    private ThreadItem threadItem;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "audio_asset_id", nullable = false)
    private AudioAsset audioAsset;

    @Column(name = "start_offset_ms", nullable = false)
    private Integer startOffsetMs = 0;

    @Column(name = "trim_start_ms", nullable = false)
    private Integer trimStartMs = 0;

    @Column(name = "trim_end_ms")
    private Integer trimEndMs;

    @Column(name = "gain_db", nullable = false, precision = 5, scale = 2)
    private BigDecimal gainDb = BigDecimal.ZERO;

    @Column(name = "pan", nullable = false, precision = 4, scale = 3)
    private BigDecimal pan = BigDecimal.ZERO;

    @Column(name = "muted", nullable = false)
    private Boolean muted = false;

    @Column(name = "instrument")
    private String instrument;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected AudioItemDetails() {
        // JPA
    }

    public AudioItemDetails(ThreadItem threadItem, AudioAsset audioAsset) {
        this.threadItem = threadItem;
        this.audioAsset = audioAsset;
    }

    @PrePersist
    void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public UUID getThreadItemId() {
        return threadItemId;
    }

    public ThreadItem getThreadItem() {
        return threadItem;
    }

    public AudioAsset getAudioAsset() {
        return audioAsset;
    }

    public Integer getStartOffsetMs() {
        return startOffsetMs;
    }

    public Integer getTrimStartMs() {
        return trimStartMs;
    }

    public Integer getTrimEndMs() {
        return trimEndMs;
    }

    public BigDecimal getGainDb() {
        return gainDb;
    }

    public BigDecimal getPan() {
        return pan;
    }

    public Boolean getMuted() {
        return muted;
    }

    public String getInstrument() {
        return instrument;
    }

    public String getNotes() {
        return notes;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setStartOffsetMs(Integer startOffsetMs) {
        this.startOffsetMs = startOffsetMs;
    }

    public void setTrimStartMs(Integer trimStartMs) {
        this.trimStartMs = trimStartMs;
    }

    public void setTrimEndMs(Integer trimEndMs) {
        this.trimEndMs = trimEndMs;
    }

    public void setGainDb(BigDecimal gainDb) {
        this.gainDb = gainDb;
    }

    public void setPan(BigDecimal pan) {
        this.pan = pan;
    }

    public void setMuted(Boolean muted) {
        this.muted = muted;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
