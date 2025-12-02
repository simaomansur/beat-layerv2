package com.beatlayer.api.social;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class LayerVoteId implements Serializable {

  @Column(name = "layer_id", nullable = false)
  private UUID layerId;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  public LayerVoteId() {
  }

  public LayerVoteId(UUID layerId, UUID userId) {
    this.layerId = layerId;
    this.userId = userId;
  }

  public UUID getLayerId() {
    return layerId;
  }

  public void setLayerId(UUID layerId) {
    this.layerId = layerId;
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
    if (!(o instanceof LayerVoteId that)) return false;
    return Objects.equals(layerId, that.layerId)
        && Objects.equals(userId, that.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(layerId, userId);
  }
}
