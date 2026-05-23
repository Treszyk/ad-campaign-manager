package com.treszyk.campaigns.application.dto;

import com.treszyk.campaigns.domain.model.AdTheme;
import java.math.BigDecimal;
import java.util.List;

public record CreateCampaignCommand(
    String name,
    List<String> keywords,
    BigDecimal bidAmount,
    BigDecimal campaignFund,
    Boolean status,
    String town,
    Double radiusKm,
    AdTheme adTheme,
    Long sellerId,
    Long productId,
    Long emeraldAccountId) {}
