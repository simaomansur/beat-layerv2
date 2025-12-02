package com.beatlayer.api.social;

import com.beatlayer.api.auth.User;
import com.beatlayer.api.jam.Jam;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
    name = "jam_favorites",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"jam_id", "user_id"})
    }
)
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

  // DB default now(), so we just read it
  @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
  private Instant createdAt;

  public JamFavorite() {
  }

  public JamFavorite(Jam jam, User user) {
    this.jam = jam;
    this.user = user;
    this.id = new JamFavoriteId(jam.getId(), user.getId());
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
