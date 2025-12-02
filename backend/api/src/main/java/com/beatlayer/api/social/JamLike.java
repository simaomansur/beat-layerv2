package com.beatlayer.api.social;

import com.beatlayer.api.auth.User;
import com.beatlayer.api.jam.Jam;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "jam_likes")
public class JamLike {

  @EmbeddedId
  private JamLikeId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("jamId")
  @JoinColumn(name = "jam_id", nullable = false)
  private Jam jam;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userId")
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  public JamLike() {
  }

  public JamLike(Jam jam, User user) {
    this.jam = jam;
    this.user = user;
    this.id = new JamLikeId(
        jam != null ? jam.getId() : null,
        user != null ? user.getId() : null
    );
  }

  @PrePersist
  public void prePersist() {
    if (createdAt == null) {
      createdAt = Instant.now();
    }
    if (id == null) {
      id = new JamLikeId();
    }
    if (jam != null && id.getJamId() == null) {
      id.setJamId(jam.getId());
    }
    if (user != null && id.getUserId() == null) {
      id.setUserId(user.getId());
    }
  }

  public JamLikeId getId() {
    return id;
  }

  public void setId(JamLikeId id) {
    this.id = id;
  }

  public Jam getJam() {
    return jam;
  }

  public void setJam(Jam jam) {
    this.jam = jam;
    if (this.id == null) {
      this.id = new JamLikeId();
    }
    if (jam != null) {
      this.id.setJamId(jam.getId());
    }
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
    if (this.id == null) {
      this.id = new JamLikeId();
    }
    if (user != null) {
      this.id.setUserId(user.getId());
    }
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
