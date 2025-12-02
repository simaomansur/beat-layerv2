package com.beatlayer.api.contest;

import com.beatlayer.api.auth.User;
import com.beatlayer.api.jam.Jam;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "jam_contest_winners")
public class JamContestWinner {

  @EmbeddedId
  private JamContestWinnerId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("jamId")
  @JoinColumn(name = "jam_id", nullable = false)
  private Jam jam;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userId")
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  // 1 = gold, 2 = silver, 3 = bronze
  @Column(name = "place", nullable = false)
  private Integer place;

  @Column(name = "awarded_at", nullable = false, updatable = false)
  private Instant awardedAt;

  public JamContestWinner() {
  }

  public JamContestWinner(Jam jam, User user, int place) {
    this.jam = jam;
    this.user = user;
    this.place = place;
    this.id = new JamContestWinnerId(
        jam != null ? jam.getId() : null,
        user != null ? user.getId() : null
    );
  }

  @PrePersist
  public void prePersist() {
    if (awardedAt == null) {
      awardedAt = Instant.now();
    }
    if (id == null) {
      id = new JamContestWinnerId();
    }
    if (jam != null && id.getJamId() == null) {
      id.setJamId(jam.getId());
    }
    if (user != null && id.getUserId() == null) {
      id.setUserId(user.getId());
    }
  }

  // getters & setters

  public JamContestWinnerId getId() {
    return id;
  }

  public void setId(JamContestWinnerId id) {
    this.id = id;
  }

  public Jam getJam() {
    return jam;
  }

  public void setJam(Jam jam) {
    this.jam = jam;
    if (this.id == null) {
      this.id = new JamContestWinnerId();
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
      this.id = new JamContestWinnerId();
    }
    if (user != null) {
      this.id.setUserId(user.getId());
    }
  }

  public Integer getPlace() {
    return place;
  }

  public void setPlace(Integer place) {
    this.place = place;
  }

  public Instant getAwardedAt() {
    return awardedAt;
  }

  public void setAwardedAt(Instant awardedAt) {
    this.awardedAt = awardedAt;
  }
}
