package com.beatlayer.api.social;

import com.beatlayer.api.auth.User;
import com.beatlayer.api.layer.Layer;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "layer_votes")
public class LayerVote {

  @EmbeddedId
  private LayerVoteId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("layerId")
  @JoinColumn(name = "layer_id", nullable = false)
  private Layer layer;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userId")
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  public LayerVote() {
  }

  public LayerVote(Layer layer, User user) {
    this.layer = layer;
    this.user = user;
    this.id = new LayerVoteId(
        layer != null ? layer.getId() : null,
        user != null ? user.getId() : null
    );
  }

  @PrePersist
  public void prePersist() {
    if (createdAt == null) {
      createdAt = Instant.now();
    }
    if (id == null) {
      id = new LayerVoteId();
    }
    if (layer != null && id.getLayerId() == null) {
      id.setLayerId(layer.getId());
    }
    if (user != null && id.getUserId() == null) {
      id.setUserId(user.getId());
    }
  }

  // getters & setters

  public LayerVoteId getId() {
    return id;
  }

  public void setId(LayerVoteId id) {
    this.id = id;
  }

  public Layer getLayer() {
    return layer;
  }

  public void setLayer(Layer layer) {
    this.layer = layer;
    if (this.id == null) {
      this.id = new LayerVoteId();
    }
    if (layer != null) {
      this.id.setLayerId(layer.getId());
    }
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
    if (this.id == null) {
      this.id = new LayerVoteId();
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
