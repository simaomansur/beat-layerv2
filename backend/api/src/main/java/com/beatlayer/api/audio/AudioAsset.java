package com.beatlayer.api.audio;

import com.beatlayer.api.user.User;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "audio_assets")
public class AudioAsset {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;

    @Column(name = "storage_locator", nullable = false, columnDefinition = "text")
    private String storageLocator;

    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    @Column(name = "duration_ms", nullable = false)
    private Integer durationMs;

    @Column(name = "sample_rate")
    private Integer sampleRate;

    @Column(name = "channels")
    private Integer channels;

    @Column(name = "bytes")
    private Long bytes;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected AudioAsset() {
        // JPA
    }

    public AudioAsset(User createdBy, String storageLocator, String mimeType, Integer durationMs) {
        this.createdBy = createdBy;
        this.storageLocator = storageLocator;
        this.mimeType = mimeType;
        this.durationMs = durationMs;
    }

    @PrePersist
    void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public String getStorageLocator() {
        return storageLocator;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Integer getDurationMs() {
        return durationMs;
    }

    public Integer getSampleRate() {
        return sampleRate;
    }

    public Integer getChannels() {
        return channels;
    }

    public Long getBytes() {
        return bytes;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setSampleRate(Integer sampleRate) {
        this.sampleRate = sampleRate;
    }

    public void setChannels(Integer channels) {
        this.channels = channels;
    }

    public void setBytes(Long bytes) {
        this.bytes = bytes;
    }
}
