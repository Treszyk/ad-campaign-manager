package com.treszyk.campaigns.infrastructure.entity;

import com.treszyk.campaigns.domain.model.AdTheme;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "campaigns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CampaignJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "campaign_keywords", joinColumns = @JoinColumn(name = "campaign_id"))
  private List<String> keywords = new ArrayList<>();

  private BigDecimal bidAmount;
  private BigDecimal campaignFund;
  private Boolean status;
  private String town;
  private Double radiusKm;

  @Enumerated(EnumType.STRING)
  private AdTheme adTheme;

  private Long sellerId;
  private Long productId;
  private Long emeraldAccountId;
}
