package com.beatlayer.api.social;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class JamLikeId implements Serializable {

  private UUID jam;
  private UUID user;

  public JamLikeId() {
  }

  public JamLikeId(UUID jam, UUID user) {
    this.jam = jam;
    this.user = user;
  }

  public UUID getJam() {
    return jam;
  }

  public void setJam(UUID jam) {
    this.jam = jam;
  }

  public UUID getUser() {
    return user;
  }

  public void setUser(UUID user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JamLikeId jamLikeId = (JamLikeId) o;
    return Objects.equals(jam, jamLikeId.jam)
        && Objects.equals(user, jamLikeId.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jam, user);
  }
}
