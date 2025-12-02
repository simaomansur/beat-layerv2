package com.beatlayer.api.jam;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class JamTagId implements Serializable {

  @Column(name = "jam_id", nullable = false)
  private UUID jamId;

  @Column(name = "tag_id", nullable = false)
  private UUID tagId;

  public JamTagId() {
  }

  public JamTagId(UUID jamId, UUID tagId) {
    this.jamId = jamId;
    this.tagId = tagId;
  }

  public UUID getJamId() {
    return jamId;
  }

  public void setJamId(UUID jamId) {
    this.jamId = jamId;
  }

  public UUID getTagId() {
    return tagId;
  }

  public void setTagId(UUID tagId) {
    this.tagId = tagId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof JamTagId that)) return false;
    return Objects.equals(jamId, that.jamId)
        && Objects.equals(tagId, that.tagId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jamId, tagId);
  }
}
