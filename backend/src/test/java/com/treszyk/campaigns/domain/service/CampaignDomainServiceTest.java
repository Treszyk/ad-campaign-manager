package com.treszyk.campaigns.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.treszyk.campaigns.domain.exception.InsufficientFundsException;
import com.treszyk.campaigns.domain.exception.ProductAlreadyCampaignedException;
import com.treszyk.campaigns.domain.model.EmeraldAccount;
import com.treszyk.campaigns.domain.model.Product;
import com.treszyk.campaigns.domain.repository.CampaignRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CampaignDomainServiceTest {

  @Mock private CampaignRepository campaignRepository;

  @InjectMocks private CampaignDomainService campaignDomainService;

  private EmeraldAccount account;
  private Product product;
  private Long sellerId;

  @BeforeEach
  void setUp() {
    sellerId = 1L;
    account = new EmeraldAccount(100L, "Standard Account", new BigDecimal("100.00"), sellerId);
    product = new Product(200L, "Sample Product", "Description", sellerId);
  }

  // validateCampaignCreation Tests

  @Test
  void validateCampaignCreation_Succeeds_WhenAllInvariantsAreValid() {
    when(campaignRepository.existsByProductId(product.getId())).thenReturn(false);

    assertDoesNotThrow(
        () ->
            campaignDomainService.validateCampaignCreation(
                account, product, sellerId, new BigDecimal("50.00")));
  }

  @Test
  void validateCampaignCreation_ThrowsIllegalArgumentException_WhenSellerDoesNotOwnAccount() {
    Long otherSellerId = 999L;

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                campaignDomainService.validateCampaignCreation(
                    account, product, otherSellerId, new BigDecimal("50.00")));
    assertTrue(exception.getMessage().contains("does not own Emerald Account"));
  }

  @Test
  void validateCampaignCreation_ThrowsIllegalArgumentException_WhenSellerDoesNotOwnProduct() {
    Product unownedProduct = new Product(200L, "Sample Product", "Description", 999L);

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                campaignDomainService.validateCampaignCreation(
                    account, unownedProduct, sellerId, new BigDecimal("50.00")));
    assertTrue(exception.getMessage().contains("does not own Product"));
  }

  @Test
  void validateCampaignCreation_ThrowsProductAlreadyCampaignedException_WhenProductHasCampaign() {
    when(campaignRepository.existsByProductId(product.getId())).thenReturn(true);

    assertThrows(
        ProductAlreadyCampaignedException.class,
        () ->
            campaignDomainService.validateCampaignCreation(
                account, product, sellerId, new BigDecimal("50.00")));
  }

  @Test
  void validateCampaignCreation_ThrowsInsufficientFundsException_WhenBudgetExceedsBalance() {
    when(campaignRepository.existsByProductId(product.getId())).thenReturn(false);

    assertThrows(
        InsufficientFundsException.class,
        () ->
            campaignDomainService.validateCampaignCreation(
                account, product, sellerId, new BigDecimal("150.00")));
  }

  // deductFunds Tests

  @Test
  void deductFunds_Succeeds_WhenBalanceIsSufficient() {
    campaignDomainService.deductFunds(account, new BigDecimal("40.00"));
    assertEquals(new BigDecimal("60.00"), account.getBalance());
  }

  @Test
  void deductFunds_ThrowsInsufficientFundsException_WhenBalanceIsInsufficient() {
    assertThrows(
        InsufficientFundsException.class,
        () -> campaignDomainService.deductFunds(account, new BigDecimal("120.00")));
  }

  // refundFunds Tests

  @Test
  void refundFunds_Succeeds() {
    campaignDomainService.refundFunds(account, new BigDecimal("30.00"));
    assertEquals(new BigDecimal("130.00"), account.getBalance());
  }

  // adjustFunds Tests

  @Test
  void adjustFunds_DeductsFunds_WhenBudgetIncreases() {
    BigDecimal oldFund = new BigDecimal("50.00");
    BigDecimal newFund = new BigDecimal("80.00");

    campaignDomainService.adjustFunds(account, oldFund, newFund);
    assertEquals(new BigDecimal("70.00"), account.getBalance());
  }

  @Test
  void adjustFunds_RefundsFunds_WhenBudgetDecreases() {
    BigDecimal oldFund = new BigDecimal("80.00");
    BigDecimal newFund = new BigDecimal("50.00");

    campaignDomainService.adjustFunds(account, oldFund, newFund);
    assertEquals(new BigDecimal("130.00"), account.getBalance());
  }

  @Test
  void adjustFunds_DoesNothing_WhenBudgetIsUnchanged() {
    BigDecimal oldFund = new BigDecimal("50.00");
    BigDecimal newFund = new BigDecimal("50.00");

    campaignDomainService.adjustFunds(account, oldFund, newFund);
    assertEquals(new BigDecimal("100.00"), account.getBalance());
  }

  @Test
  void adjustFunds_ThrowsInsufficientFundsException_WhenBudgetIncreaseExceedsBalance() {
    BigDecimal oldFund = new BigDecimal("50.00");
    BigDecimal newFund = new BigDecimal("200.00");

    assertThrows(
        InsufficientFundsException.class,
        () -> campaignDomainService.adjustFunds(account, oldFund, newFund));
  }

  // validateTownAndKeywords Tests

  @Test
  void validateTownAndKeywords_Succeeds_WhenTownAndKeywordsAreSupported() {
    assertDoesNotThrow(
        () ->
            campaignDomainService.validateTownAndKeywords(
                "Warszawa", List.of("headphones", "wireless")));
  }

  @Test
  void validateTownAndKeywords_Succeeds_WhenTownOrKeywordsAreNull() {
    assertDoesNotThrow(() -> campaignDomainService.validateTownAndKeywords(null, null));
  }

  @Test
  void validateTownAndKeywords_ThrowsIllegalArgumentException_WhenTownIsNotSupported() {
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                campaignDomainService.validateTownAndKeywords("Sosnowiec", List.of("headphones")));
    assertTrue(exception.getMessage().contains("Town 'Sosnowiec' is not supported"));
  }

  @Test
  void validateTownAndKeywords_ThrowsIllegalArgumentException_WhenKeywordIsNotSupported() {
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                campaignDomainService.validateTownAndKeywords(
                    "Warszawa", List.of("headphones", "illegal")));
    assertTrue(exception.getMessage().contains("Keyword 'illegal' is not supported"));
  }
}
