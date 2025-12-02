package com.beatlayer.api.social;

import com.beatlayer.api.auth.User;
import com.beatlayer.api.jam.Jam;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@IdClass(JamLikeId.class)
@Table(
    name = "jam_likes",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_jam_likes_jam_user",
            columnNames = { "jam_id", "user_id" }
        )
    }
)
public class JamLike {

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "jam_id", nullable = false)
  private Jam jam;

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @PrePersist
  public void prePersist() {
    if (createdAt == null) {
      createdAt = Instant.now();
    }
  }

  // getters / setters

  public Jam getJam() {
    return jam;
  }

  public void setJam(Jam jam) {
    this.jam = jam;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
