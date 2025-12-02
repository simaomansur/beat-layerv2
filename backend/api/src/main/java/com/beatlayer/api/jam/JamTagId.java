package com.beatlayer.api.jam;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class JamTagId implements Serializable {

  private UUID jam;
  private UUID tag;

  public JamTagId() {}

  public JamTagId(UUID jam, UUID tag) {
    this.jam = jam;
    this.tag = tag;
  }

  public UUID getJam() {
    return jam;
  }

  public void setJam(UUID jam) {
    this.jam = jam;
  }

  public UUID getTag() {
    return tag;
  }

  public void setTag(UUID tag) {
    this.tag = tag;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof JamTagId that)) return false;
    return Objects.equals(jam, that.jam) &&
           Objects.equals(tag, that.tag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jam, tag);
  }
}
