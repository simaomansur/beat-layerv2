package com.beatlayer.api.jam;

import jakarta.persistence.*;

@Entity
@Table(name = "jam_tags")
public class JamTag {

  @EmbeddedId
  private JamTagId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("jamId")
  @JoinColumn(name = "jam_id", nullable = false)
  private Jam jam;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("tagId")
  @JoinColumn(name = "tag_id", nullable = false)
  private Tag tag;

  public JamTag() {
  }

  public JamTag(Jam jam, Tag tag) {
    this.jam = jam;
    this.tag = tag;
    this.id = new JamTagId(
        jam != null ? jam.getId() : null,
        tag != null ? tag.getId() : null
    );
  }

  public JamTagId getId() {
    return id;
  }

  public void setId(JamTagId id) {
    this.id = id;
  }

  public Jam getJam() {
    return jam;
  }

  public void setJam(Jam jam) {
    this.jam = jam;
    if (this.id == null) {
      this.id = new JamTagId();
    }
    if (jam != null) {
      this.id.setJamId(jam.getId());
    }
  }

  public Tag getTag() {
    return tag;
  }

  public void setTag(Tag tag) {
    this.tag = tag;
    if (this.id == null) {
      this.id = new JamTagId();
    }
    if (tag != null) {
      this.id.setTagId(tag.getId());
    }
  }
}
