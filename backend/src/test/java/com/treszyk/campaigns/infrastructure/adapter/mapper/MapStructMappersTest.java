package com.treszyk.campaigns.infrastructure.adapter.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.treszyk.campaigns.domain.model.AdTheme;
import com.treszyk.campaigns.domain.model.Campaign;
import com.treszyk.campaigns.domain.model.EmeraldAccount;
import com.treszyk.campaigns.domain.model.Product;
import com.treszyk.campaigns.domain.model.Seller;
import com.treszyk.campaigns.infrastructure.entity.CampaignJpaEntity;
import com.treszyk.campaigns.infrastructure.entity.EmeraldAccountJpaEntity;
import com.treszyk.campaigns.infrastructure.entity.ProductJpaEntity;
import com.treszyk.campaigns.infrastructure.entity.SellerJpaEntity;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class MapStructMappersTest {

  private final SellerMapper sellerMapper = Mappers.getMapper(SellerMapper.class);
  private final EmeraldAccountMapper emeraldAccountMapper =
      Mappers.getMapper(EmeraldAccountMapper.class);
  private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
  private final CampaignMapper campaignMapper = Mappers.getMapper(CampaignMapper.class);

  @Test
  void sellerMapper_ShouldMapBidirectionally() {
    Seller domain = new Seller(1L, "John", "Doe");
    SellerJpaEntity entity = sellerMapper.toEntity(domain);

    assertNotNull(entity);
    assertEquals(1L, entity.getId());
    assertEquals("John", entity.getFirstName());
    assertEquals("Doe", entity.getLastName());

    Seller mappedDomain = sellerMapper.toDomain(entity);
    assertNotNull(mappedDomain);
    assertEquals(1L, mappedDomain.getId());
    assertEquals("John", mappedDomain.getFirstName());
    assertEquals("Doe", mappedDomain.getLastName());
  }

  @Test
  void emeraldAccountMapper_ShouldMapBidirectionally() {
    EmeraldAccount domain = new EmeraldAccount(1L, "Primary Wallet", BigDecimal.valueOf(100.0), 2L);
    EmeraldAccountJpaEntity entity = emeraldAccountMapper.toEntity(domain);

    assertNotNull(entity);
    assertEquals(1L, entity.getId());
    assertEquals("Primary Wallet", entity.getAccountName());
    assertEquals(BigDecimal.valueOf(100.0), entity.getBalance());
    assertEquals(2L, entity.getSellerId());

    EmeraldAccount mappedDomain = emeraldAccountMapper.toDomain(entity);
    assertNotNull(mappedDomain);
    assertEquals(1L, mappedDomain.getId());
    assertEquals("Primary Wallet", mappedDomain.getAccountName());
    assertEquals(BigDecimal.valueOf(100.0), mappedDomain.getBalance());
    assertEquals(2L, mappedDomain.getSellerId());
  }

  @Test
  void productMapper_ShouldMapBidirectionally() {
    Product domain = new Product(1L, "Headphones", "Noise cancelling", 3L);
    ProductJpaEntity entity = productMapper.toEntity(domain);

    assertNotNull(entity);
    assertEquals(1L, entity.getId());
    assertEquals("Headphones", entity.getName());
    assertEquals("Noise cancelling", entity.getDescription());
    assertEquals(3L, entity.getSellerId());

    Product mappedDomain = productMapper.toDomain(entity);
    assertNotNull(mappedDomain);
    assertEquals(1L, mappedDomain.getId());
    assertEquals("Headphones", mappedDomain.getName());
    assertEquals("Noise cancelling", mappedDomain.getDescription());
    assertEquals(3L, mappedDomain.getSellerId());
  }

  @Test
  void campaignMapper_ShouldMapBidirectionally() {
    Campaign domain =
        new Campaign(
            1L,
            "Summer Campaign",
            List.of("wireless", "premium"),
            BigDecimal.valueOf(2.5),
            BigDecimal.valueOf(500.0),
            true,
            "Warszawa",
            10.0,
            AdTheme.PASTEL_MINT,
            3L,
            4L,
            5L);

    CampaignJpaEntity entity = campaignMapper.toEntity(domain);

    assertNotNull(entity);
    assertEquals(1L, entity.getId());
    assertEquals("Summer Campaign", entity.getName());
    assertEquals(List.of("wireless", "premium"), entity.getKeywords());
    assertEquals(BigDecimal.valueOf(2.5), entity.getBidAmount());
    assertEquals(BigDecimal.valueOf(500.0), entity.getCampaignFund());
    assertTrue(entity.getStatus());
    assertEquals("Warszawa", entity.getTown());
    assertEquals(10.0, entity.getRadiusKm());
    assertEquals(AdTheme.PASTEL_MINT, entity.getAdTheme());
    assertEquals(3L, entity.getSellerId());
    assertEquals(4L, entity.getProductId());
    assertEquals(5L, entity.getEmeraldAccountId());

    Campaign mappedDomain = campaignMapper.toDomain(entity);
    assertNotNull(mappedDomain);
    assertEquals(1L, mappedDomain.getId());
    assertEquals("Summer Campaign", mappedDomain.getName());
    assertEquals(List.of("wireless", "premium"), mappedDomain.getKeywords());
    assertEquals(BigDecimal.valueOf(2.5), mappedDomain.getBidAmount());
    assertEquals(BigDecimal.valueOf(500.0), mappedDomain.getCampaignFund());
    assertTrue(mappedDomain.getStatus());
    assertEquals("Warszawa", mappedDomain.getTown());
    assertEquals(10.0, mappedDomain.getRadiusKm());
    assertEquals(AdTheme.PASTEL_MINT, mappedDomain.getAdTheme());
    assertEquals(3L, mappedDomain.getSellerId());
    assertEquals(4L, mappedDomain.getProductId());
    assertEquals(5L, mappedDomain.getEmeraldAccountId());
  }

  @Test
  void campaignMapper_ShouldEnforceDomainValidation_WhenBidAmountIsTooLow() {
    CampaignJpaEntity invalidEntity =
        new CampaignJpaEntity(
            1L,
            "Invalid Bid Campaign",
            List.of("wireless"),
            BigDecimal.valueOf(0.5), // Bid amount under 1.0 is illegal
            BigDecimal.valueOf(100.0),
            true,
            "Warszawa",
            5.0,
            AdTheme.PASTEL_MINT,
            3L,
            4L,
            5L);

    assertThrows(IllegalArgumentException.class, () -> campaignMapper.toDomain(invalidEntity));
  }

  @Test
  void campaignMapper_ShouldEnforceDomainValidation_WhenCampaignFundIsNegative() {
    CampaignJpaEntity invalidEntity =
        new CampaignJpaEntity(
            1L,
            "Invalid Fund Campaign",
            List.of("wireless"),
            BigDecimal.valueOf(2.0),
            BigDecimal.valueOf(-50.0), // Negative fund is illegal
            true,
            "Warszawa",
            5.0,
            AdTheme.PASTEL_MINT,
            3L,
            4L,
            5L);

    assertThrows(IllegalArgumentException.class, () -> campaignMapper.toDomain(invalidEntity));
  }

  @Test
  void campaignMapper_ShouldEnforceDomainValidation_WhenRadiusIsNegative() {
    CampaignJpaEntity invalidEntity =
        new CampaignJpaEntity(
            1L,
            "Invalid Radius Campaign",
            List.of("wireless"),
            BigDecimal.valueOf(2.0),
            BigDecimal.valueOf(100.0),
            true,
            "Warszawa",
            -5.0, // Negative radius is illegal
            AdTheme.PASTEL_MINT,
            3L,
            4L,
            5L);

    assertThrows(IllegalArgumentException.class, () -> campaignMapper.toDomain(invalidEntity));
  }

  @Test
  void emeraldAccountMapper_ShouldEnforceDomainValidation_WhenBalanceIsNegative() {
    EmeraldAccountJpaEntity invalidEntity =
        new EmeraldAccountJpaEntity(1L, "Invalid Account", BigDecimal.valueOf(-10.0), 2L);

    assertThrows(
        IllegalArgumentException.class, () -> emeraldAccountMapper.toDomain(invalidEntity));
  }
}
