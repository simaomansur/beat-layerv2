package com.beatlayer.api.jam;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@IdClass(JamTagId.class)
@Table(name = "jam_tags")
public class JamTag {

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "jam_id", nullable = false)
  private Jam jam;

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tag_id", nullable = false)
  private Tag tag;

  public JamTag() {}

  public JamTag(Jam jam, Tag tag) {
    this.jam = jam;
    this.tag = tag;
  }

  public Jam getJam() {
    return jam;
  }

  public void setJam(Jam jam) {
    this.jam = jam;
  }

  public Tag getTag() {
    return tag;
  }

  public void setTag(Tag tag) {
    this.tag = tag;
  }
}
