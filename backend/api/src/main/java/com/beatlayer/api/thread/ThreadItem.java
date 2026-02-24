package com.beatlayer.api.thread;

import com.beatlayer.api.jam.Jam;
import com.beatlayer.api.user.User;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "thread_items")
public class ThreadItem {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "jam_id", nullable = false)
    private Jam jam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_item_id")
    private ThreadItem parent;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;

    @Column(name = "item_type", nullable = false)
    private String itemType; // "AUDIO" or "COMMENT"

    @Column(columnDefinition = "text")
    private String body; // for COMMENT (null ok for AUDIO)

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected ThreadItem() {
        // JPA
    }

    public static ThreadItem newAudio(Jam jam, ThreadItem parent, User createdBy) {
        ThreadItem t = new ThreadItem();
        t.jam = jam;
        t.parent = parent;
        t.createdBy = createdBy;
        t.itemType = "AUDIO";
        t.body = null;
        return t;
    }

    public static ThreadItem newComment(Jam jam, ThreadItem parent, User createdBy, String body) {
        ThreadItem t = new ThreadItem();
        t.jam = jam;
        t.parent = parent;
        t.createdBy = createdBy;
        t.itemType = "COMMENT";
        t.body = body;
        return t;
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

    public Jam getJam() {
        return jam;
    }

    public ThreadItem getParent() {
        return parent;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public String getItemType() {
        return itemType;
    }

    public String getBody() {
        return body;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
