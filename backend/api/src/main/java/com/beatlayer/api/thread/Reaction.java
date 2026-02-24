package com.beatlayer.api.thread;

import com.beatlayer.api.user.User;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "reactions",
        uniqueConstraints = {
                @UniqueConstraint(name = "ux_reactions_user_target_type",
                        columnNames = {"user_id", "target_thread_item_id", "reaction_type"})
        }
)
public class Reaction {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "target_thread_item_id", nullable = false)
    private ThreadItem target;

    @Column(name = "reaction_type", nullable = false)
    private String reactionType = "LIKE";

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected Reaction() {
        // JPA
    }

    public Reaction(User user, ThreadItem target) {
        this.user = user;
        this.target = target;
        this.reactionType = "LIKE";
    }

    @PrePersist
    void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public ThreadItem getTarget() {
        return target;
    }

    public String getReactionType() {
        return reactionType;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
