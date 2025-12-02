package com.beatlayer.api.stripe;

import com.beatlayer.api.auth.User;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "stripe_payments")
public class StripePayment {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "stripe_payment_intent_id")
  private String stripePaymentIntentId;

  @Column(name = "stripe_invoice_id")
  private String stripeInvoiceId;

  @Column(name = "stripe_checkout_session_id")
  private String stripeCheckoutSessionId;

  @Column(name = "amount_total")
  private Integer amountTotal;

  @Column
  private String currency;

  @Column(nullable = false)
  private String purpose; // 'credits' or 'creator_subscription'

  @Column(name = "raw_data", columnDefinition = "jsonb")
  private String rawData;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @PrePersist
  public void prePersist() {
    if (id == null) {
      id = UUID.randomUUID();
    }
    if (createdAt == null) {
      createdAt = Instant.now();
    }
  }

  // getters & setters

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getStripePaymentIntentId() {
    return stripePaymentIntentId;
  }

  public void setStripePaymentIntentId(String stripePaymentIntentId) {
    this.stripePaymentIntentId = stripePaymentIntentId;
  }

  public String getStripeInvoiceId() {
    return stripeInvoiceId;
  }

  public void setStripeInvoiceId(String stripeInvoiceId) {
    this.stripeInvoiceId = stripeInvoiceId;
  }

  public String getStripeCheckoutSessionId() {
    return stripeCheckoutSessionId;
  }

  public void setStripeCheckoutSessionId(String stripeCheckoutSessionId) {
    this.stripeCheckoutSessionId = stripeCheckoutSessionId;
  }

  public Integer getAmountTotal() {
    return amountTotal;
  }

  public void setAmountTotal(Integer amountTotal) {
    this.amountTotal = amountTotal;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  public String getRawData() {
    return rawData;
  }

  public void setRawData(String rawData) {
    this.rawData = rawData;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
