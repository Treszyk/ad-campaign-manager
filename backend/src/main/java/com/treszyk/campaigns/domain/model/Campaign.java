package com.treszyk.campaigns.domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Campaign {
  private Long id;
  @NonNull private String name;

  @NonNull private List<String> keywords = new ArrayList<>();

  @NonNull private BigDecimal bidAmount;
  @NonNull private BigDecimal campaignFund;
  @NonNull private Boolean status;
  private String town; // not a mandatory field
  @NonNull private Double radiusKm;

  @NonNull private AdTheme adTheme; // the background theme being displayed on preview

  @NonNull private Long sellerId;
  @NonNull private Long productId;
  @NonNull private Long emeraldAccountId;

  public Campaign(
      Long id,
      @NonNull String name,
      @NonNull List<String> keywords,
      @NonNull BigDecimal bidAmount,
      @NonNull BigDecimal campaignFund,
      @NonNull Boolean status,
      String town,
      @NonNull Double radiusKm,
      @NonNull AdTheme adTheme,
      @NonNull Long sellerId,
      @NonNull Long productId,
      @NonNull Long emeraldAccountId) {
    this.id = id;
    this.name = name;
    this.keywords = keywords;
    setBidAmount(bidAmount);
    setCampaignFund(campaignFund);
    this.status = status;
    this.town = town;
    setRadiusKm(radiusKm);
    this.adTheme = adTheme;
    this.sellerId = sellerId;
    this.productId = productId;
    this.emeraldAccountId = emeraldAccountId;
  }

  public void setBidAmount(@NonNull BigDecimal bidAmount) {
    if (bidAmount.compareTo(BigDecimal.ONE) < 0)
      throw new IllegalArgumentException("BidAmount must be higher or equal to 1.0");
    this.bidAmount = bidAmount;
  }

  public void setCampaignFund(@NonNull BigDecimal campaignFund) {
    if (campaignFund.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Campaign fund must be positive");
    }
    this.campaignFund = campaignFund;
  }

  public void setRadiusKm(@NonNull Double radiusKm) {
    if (radiusKm < 0) {
      throw new IllegalArgumentException("Radius cannot be negative");
    }
    this.radiusKm = radiusKm;
  }
}
