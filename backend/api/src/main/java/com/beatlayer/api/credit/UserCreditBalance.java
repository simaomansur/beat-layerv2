package com.beatlayer.api.credit;

import com.beatlayer.api.auth.User;
import jakarta.persistence.*;

@Entity
@Table(name = "user_credit_balances")
public class UserCreditBalance {

  @Id
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private Integer balance = 0;

  public UserCreditBalance() {
  }

  public UserCreditBalance(User user, int balance) {
    this.user = user;
    this.balance = balance;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Integer getBalance() {
    return balance;
  }

  public void setBalance(Integer balance) {
    this.balance = balance;
  }
}
