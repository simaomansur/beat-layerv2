package com.beatlayer.api.credit;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "credit_packages")
public class CreditPackage {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String code;

  @Column(nullable = false)
  private String name;

  @Column(name = "credits_amount", nullable = false)
  private Integer creditsAmount;

  @Column(name = "stripe_price_id", nullable = false)
  private String stripePriceId;

  @Column(nullable = false)
  private boolean active = true;

  @PrePersist
  public void prePersist() {
    if (id == null) {
      id = UUID.randomUUID();
    }
  }

  // getters & setters

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getCreditsAmount() {
    return creditsAmount;
  }

  public void setCreditsAmount(Integer creditsAmount) {
    this.creditsAmount = creditsAmount;
  }

  public String getStripePriceId() {
    return stripePriceId;
  }

  public void setStripePriceId(String stripePriceId) {
    this.stripePriceId = stripePriceId;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
