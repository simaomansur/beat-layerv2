package com.beatlayer.api.jam;

import com.beatlayer.api.user.User;
import com.beatlayer.api.thread.ThreadItem;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "jams")
public class Jam {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "loop_length_ms", nullable = false)
    private Integer loopLengthMs;

    @Column
    private Integer bpm;

    @Column(name = "musical_key")
    private String musicalKey;

    @Column
    private String genre;

    @Column(name = "instrument_hint")
    private String instrumentHint;

    @Column(nullable = false)
    private String visibility = "public";

    // Nullable during creation; backend will set after root is created.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_item_id")
    private ThreadItem rootItem;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected Jam() {
        // JPA
    }

    public Jam(User createdBy, String title, Integer loopLengthMs) {
        this.createdBy = createdBy;
        this.title = title;
        this.loopLengthMs = loopLengthMs;
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

    public UUID getId() {
        return id;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getLoopLengthMs() {
        return loopLengthMs;
    }

    public Integer getBpm() {
        return bpm;
    }

    public String getMusicalKey() {
        return musicalKey;
    }

    public String getGenre() {
        return genre;
    }

    public String getInstrumentHint() {
        return instrumentHint;
    }

    public String getVisibility() {
        return visibility;
    }

    public ThreadItem getRootItem() {
        return rootItem;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLoopLengthMs(Integer loopLengthMs) {
        this.loopLengthMs = loopLengthMs;
    }

    public void setBpm(Integer bpm) {
        this.bpm = bpm;
    }

    public void setMusicalKey(String musicalKey) {
        this.musicalKey = musicalKey;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setInstrumentHint(String instrumentHint) {
        this.instrumentHint = instrumentHint;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public void setRootItem(ThreadItem rootItem) {
        this.rootItem = rootItem;
    }
}