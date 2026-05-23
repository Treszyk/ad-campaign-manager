package com.treszyk.campaigns.domain.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmeraldAccount {
  private Long id;
  @NonNull private String accountName;
  @NonNull private BigDecimal balance;

  // one seller is able to hold many EmeraldAccounts
  @NonNull private Long sellerId;

  public void setBalance(@NonNull BigDecimal balance) {
    if (balance.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Account balance cannot be negative");
    }
    this.balance = balance;
  }
}
