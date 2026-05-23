package com.treszyk.campaigns.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.treszyk.campaigns.application.dto.CreateCampaignCommand;
import com.treszyk.campaigns.application.dto.UpdateCampaignCommand;
import com.treszyk.campaigns.domain.exception.InsufficientFundsException;
import com.treszyk.campaigns.domain.exception.ResourceNotFoundException;
import com.treszyk.campaigns.domain.model.AdTheme;
import com.treszyk.campaigns.domain.model.Campaign;
import com.treszyk.campaigns.domain.model.EmeraldAccount;
import com.treszyk.campaigns.domain.model.Product;
import com.treszyk.campaigns.domain.model.Seller;
import com.treszyk.campaigns.domain.repository.CampaignRepository;
import com.treszyk.campaigns.domain.repository.EmeraldAccountRepository;
import com.treszyk.campaigns.domain.repository.ProductRepository;
import com.treszyk.campaigns.domain.repository.SellerRepository;
import com.treszyk.campaigns.domain.service.CampaignDomainService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CampaignApplicationServiceTest {

  @Mock private CampaignRepository campaignRepository;
  @Mock private EmeraldAccountRepository emeraldAccountRepository;
  @Mock private ProductRepository productRepository;
  @Mock private SellerRepository sellerRepository;
  @Mock private CampaignDomainService campaignDomainService;

  @InjectMocks private CampaignApplicationService service;

  @Test
  void createCampaign_Succeeds_WhenCommandIsValid() {
    CreateCampaignCommand cmd =
        new CreateCampaignCommand(
            "Summer Ad",
            List.of("summer", "sale"),
            BigDecimal.valueOf(2.0),
            BigDecimal.valueOf(100.0),
            true,
            "Warsaw",
            10.0,
            AdTheme.PASTEL_MINT,
            1L,
            2L,
            3L);

    EmeraldAccount account = new EmeraldAccount(3L, "Main Wallet", BigDecimal.valueOf(500.0), 1L);
    Product product = new Product(2L, "Sunglasses", "Stylish sunglasses", 1L);
    Seller seller = new Seller(1L, "John", "Doe");
    Campaign expectedCampaign =
        new Campaign(
            1L,
            cmd.name(),
            cmd.keywords(),
            cmd.bidAmount(),
            cmd.campaignFund(),
            cmd.status(),
            cmd.town(),
            cmd.radiusKm(),
            cmd.adTheme(),
            cmd.sellerId(),
            cmd.productId(),
            cmd.emeraldAccountId());

    when(emeraldAccountRepository.findById(3L)).thenReturn(Optional.of(account));
    when(productRepository.findById(2L)).thenReturn(Optional.of(product));
    when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
    when(campaignRepository.save(any(Campaign.class))).thenReturn(expectedCampaign);

    Campaign result = service.createCampaign(cmd);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getName()).isEqualTo("Summer Ad");

    verify(campaignDomainService)
        .validateCampaignCreation(account, product, 1L, BigDecimal.valueOf(100.0));
    verify(campaignDomainService).deductFunds(account, BigDecimal.valueOf(100.0));
    verify(emeraldAccountRepository).save(account);
    verify(campaignRepository).save(any(Campaign.class));
  }

  @Test
  void createCampaign_ThrowsInsufficientFundsException_WhenBalanceIsInsufficient() {
    CreateCampaignCommand cmd =
        new CreateCampaignCommand(
            "Summer Ad",
            List.of("summer"),
            BigDecimal.valueOf(2.0),
            BigDecimal.valueOf(100.0),
            true,
            "Warsaw",
            10.0,
            AdTheme.PASTEL_MINT,
            1L,
            2L,
            3L);

    EmeraldAccount account = new EmeraldAccount(3L, "Main Wallet", BigDecimal.valueOf(10.0), 1L);
    Product product = new Product(2L, "Sunglasses", "Stylish sunglasses", 1L);
    Seller seller = new Seller(1L, "John", "Doe");

    when(emeraldAccountRepository.findById(3L)).thenReturn(Optional.of(account));
    when(productRepository.findById(2L)).thenReturn(Optional.of(product));
    when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
    doThrow(new InsufficientFundsException(BigDecimal.valueOf(100.0), BigDecimal.valueOf(10.0)))
        .when(campaignDomainService)
        .validateCampaignCreation(account, product, 1L, BigDecimal.valueOf(100.0));

    assertThatThrownBy(() -> service.createCampaign(cmd))
        .isInstanceOf(InsufficientFundsException.class);

    verify(campaignDomainService, never()).deductFunds(any(), any());
    verify(emeraldAccountRepository, never()).save(any());
    verify(campaignRepository, never()).save(any());
  }

  @Test
  void updateCampaign_Succeeds_WhenCommandIsValid() {
    UpdateCampaignCommand cmd =
        new UpdateCampaignCommand(
            1L,
            "Updated Ad",
            List.of("updated"),
            BigDecimal.valueOf(3.0),
            BigDecimal.valueOf(150.0),
            true,
            "Cracow",
            15.0,
            AdTheme.PASTEL_BLUE);

    Campaign existingCampaign =
        new Campaign(
            1L,
            "Summer Ad",
            List.of("summer"),
            BigDecimal.valueOf(2.0),
            BigDecimal.valueOf(100.0),
            true,
            "Warsaw",
            10.0,
            AdTheme.PASTEL_MINT,
            1L,
            2L,
            3L);
    EmeraldAccount account = new EmeraldAccount(3L, "Main Wallet", BigDecimal.valueOf(500.0), 1L);

    when(campaignRepository.findById(1L)).thenReturn(Optional.of(existingCampaign));
    when(emeraldAccountRepository.findById(3L)).thenReturn(Optional.of(account));
    when(campaignRepository.save(existingCampaign)).thenReturn(existingCampaign);

    Campaign result = service.updateCampaign(cmd);

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("Updated Ad");
    assertThat(result.getCampaignFund()).isEqualTo(BigDecimal.valueOf(150.0));

    verify(campaignDomainService)
        .adjustFunds(account, BigDecimal.valueOf(100.0), BigDecimal.valueOf(150.0));
    verify(emeraldAccountRepository).save(account);
    verify(campaignRepository).save(existingCampaign);
  }

  @Test
  void deleteCampaign_Succeeds_WhenCampaignExists() {
    Campaign existingCampaign =
        new Campaign(
            1L,
            "Summer Ad",
            List.of("summer"),
            BigDecimal.valueOf(2.0),
            BigDecimal.valueOf(100.0),
            true,
            "Warsaw",
            10.0,
            AdTheme.PASTEL_MINT,
            1L,
            2L,
            3L);
    EmeraldAccount account = new EmeraldAccount(3L, "Main Wallet", BigDecimal.valueOf(500.0), 1L);

    when(campaignRepository.findById(1L)).thenReturn(Optional.of(existingCampaign));
    when(emeraldAccountRepository.findById(3L)).thenReturn(Optional.of(account));

    service.deleteCampaign(1L);

    verify(campaignDomainService).refundFunds(account, BigDecimal.valueOf(100.0));
    verify(emeraldAccountRepository).save(account);
    verify(campaignRepository).deleteById(1L);
  }

  @Test
  void getCampaignById_ReturnsCampaign_WhenCampaignExists() {
    Campaign campaign =
        new Campaign(
            1L,
            "Summer Ad",
            List.of("summer"),
            BigDecimal.valueOf(2.0),
            BigDecimal.valueOf(100.0),
            true,
            "Warsaw",
            10.0,
            AdTheme.PASTEL_MINT,
            1L,
            2L,
            3L);

    when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign));

    Campaign result = service.getCampaignById(1L);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1L);
  }

  @Test
  void getCampaignById_ThrowsResourceNotFoundException_WhenCampaignDoesNotExist() {
    when(campaignRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.getCampaignById(99L))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Campaign not found with ID: 99");
  }
}
