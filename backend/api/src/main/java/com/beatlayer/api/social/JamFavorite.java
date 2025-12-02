package com.beatlayer.api.social;

import com.beatlayer.api.auth.User;
import com.beatlayer.api.jam.Jam;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "jam_favorites")
public class JamFavorite {

  @EmbeddedId
  private JamFavoriteId id;

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

  public JamFavorite() {
  }

  public JamFavorite(Jam jam, User user) {
    this.jam = jam;
    this.user = user;
    this.id = new JamFavoriteId(
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
      id = new JamFavoriteId();
    }
    if (jam != null && id.getJamId() == null) {
      id.setJamId(jam.getId());
    }
    if (user != null && id.getUserId() == null) {
      id.setUserId(user.getId());
    }
  }

  public JamFavoriteId getId() {
    return id;
  }

  public void setId(JamFavoriteId id) {
    this.id = id;
  }

  public Jam getJam() {
    return jam;
  }

  public void setJam(Jam jam) {
    this.jam = jam;
    if (this.id == null) {
      this.id = new JamFavoriteId();
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
      this.id = new JamFavoriteId();
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
