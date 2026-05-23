package com.treszyk.campaigns.presentation.dto;

import java.math.BigDecimal;

public record EmeraldAccountResponse(
    Long id, String accountName, BigDecimal balance, Long sellerId) {}
