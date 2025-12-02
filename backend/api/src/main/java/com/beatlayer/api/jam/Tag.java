package com.beatlayer.api.jam;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tags")
public class Tag {

  @Id
  @GeneratedValue
  @Column(nullable = false, updatable = false)
  private UUID id;

  @Column(name = "name", nullable = false, length = 64, unique = true)
  private String name;

  // ===== Getters & Setters =====

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
