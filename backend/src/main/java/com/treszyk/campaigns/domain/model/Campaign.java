package com.treszyk.campaigns.domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
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

  public void setBidAmount(BigDecimal bidAmount) {
    if (bidAmount == null || bidAmount.compareTo(BigDecimal.ONE) < 0)
      throw new IllegalArgumentException("BidAmount must be higher or equal to 1.0 and not null");
    this.bidAmount = bidAmount;
  }

  public void setCampaignFund(BigDecimal campaignFund) {
    if (campaignFund == null || campaignFund.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Campaign cannot be negative or null");
    }
    this.campaignFund = campaignFund;
  }

  public void setRadiusKm(Double radiusKm) {
    if (radiusKm == null || radiusKm < 0) {
      throw new IllegalArgumentException("Radius cannot be negative or null");
    }
    this.radiusKm = radiusKm;
  }
}
