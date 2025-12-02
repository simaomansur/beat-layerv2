package com.beatlayer.api.social;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class LayerVoteId implements Serializable {

  private UUID layerId;
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
    if (o == null || getClass() != o.getClass()) return false;
    LayerVoteId that = (LayerVoteId) o;
    return Objects.equals(layerId, that.layerId)
        && Objects.equals(userId, that.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(layerId, userId);
  }
}
