package com.beatlayer.api.social;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class JamFavoriteId implements Serializable {

  @Column(name = "jam_id", nullable = false)
  private UUID jamId;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  public JamFavoriteId() {
  }

  public JamFavoriteId(UUID jamId, UUID userId) {
    this.jamId = jamId;
    this.userId = userId;
  }

  public UUID getJamId() {
    return jamId;
  }

  public void setJamId(UUID jamId) {
    this.jamId = jamId;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof JamFavoriteId that)) return false;
    return Objects.equals(jamId, that.jamId)
        && Objects.equals(userId, that.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jamId, userId);
  }
}
