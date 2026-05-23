package com.treszyk.campaigns.domain.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
  public InsufficientFundsException(BigDecimal required, BigDecimal available) {
    super(
        String.format(
            "Insufficient funds: required %s, but only %s available", required, available));
  }
}
