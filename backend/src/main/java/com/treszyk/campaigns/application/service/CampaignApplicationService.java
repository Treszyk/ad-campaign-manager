package com.treszyk.campaigns.application.service;

import com.treszyk.campaigns.application.dto.CreateCampaignCommand;
import com.treszyk.campaigns.application.dto.UpdateCampaignCommand;
import com.treszyk.campaigns.application.usecase.CreateCampaignUseCase;
import com.treszyk.campaigns.application.usecase.DeleteCampaignUseCase;
import com.treszyk.campaigns.application.usecase.GetCampaignsUseCase;
import com.treszyk.campaigns.application.usecase.UpdateCampaignUseCase;
import com.treszyk.campaigns.domain.exception.ResourceNotFoundException;
import com.treszyk.campaigns.domain.model.Campaign;
import com.treszyk.campaigns.domain.model.EmeraldAccount;
import com.treszyk.campaigns.domain.model.Product;
import com.treszyk.campaigns.domain.model.Seller;
import com.treszyk.campaigns.domain.repository.CampaignRepository;
import com.treszyk.campaigns.domain.repository.EmeraldAccountRepository;
import com.treszyk.campaigns.domain.repository.ProductRepository;
import com.treszyk.campaigns.domain.repository.SellerRepository;
import com.treszyk.campaigns.domain.service.CampaignDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CampaignApplicationService
    implements CreateCampaignUseCase,
        UpdateCampaignUseCase,
        DeleteCampaignUseCase,
        GetCampaignsUseCase {

  private final CampaignRepository campaignRepository;
  private final EmeraldAccountRepository emeraldAccountRepository;
  private final ProductRepository productRepository;
  private final SellerRepository sellerRepository;
  private final CampaignDomainService campaignDomainService;

  @Override
  public Campaign createCampaign(CreateCampaignCommand cmd) {
    EmeraldAccount account = getAccountOrThrow(cmd.emeraldAccountId());
    Product product = getProductOrThrow(cmd.productId());
    Seller seller = getSellerOrThrow(cmd.sellerId());

    campaignDomainService.validateCampaignCreation(
        account, product, seller.getId(), cmd.campaignFund());

    campaignDomainService.deductFunds(account, cmd.campaignFund());
    emeraldAccountRepository.save(account);

    Campaign campaign =
        new Campaign(
            null,
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

    return campaignRepository.save(campaign);
  }

  @Override
  public Campaign updateCampaign(UpdateCampaignCommand cmd) {
    Campaign campaign = getCampaignOrThrow(cmd.id());
    EmeraldAccount account = getAccountOrThrow(campaign.getEmeraldAccountId());

    campaignDomainService.adjustFunds(account, campaign.getCampaignFund(), cmd.campaignFund());
    emeraldAccountRepository.save(account);

    campaign.setName(cmd.name());
    campaign.setKeywords(cmd.keywords());
    campaign.setBidAmount(cmd.bidAmount());
    campaign.setCampaignFund(cmd.campaignFund());
    campaign.setStatus(cmd.status());
    campaign.setTown(cmd.town());
    campaign.setRadiusKm(cmd.radiusKm());
    campaign.setAdTheme(cmd.adTheme());

    return campaignRepository.save(campaign);
  }

  @Override
  public void deleteCampaign(Long id) {
    Campaign campaign = getCampaignOrThrow(id);
    EmeraldAccount account = getAccountOrThrow(campaign.getEmeraldAccountId());

    campaignDomainService.refundFunds(account, campaign.getCampaignFund());
    emeraldAccountRepository.save(account);

    campaignRepository.deleteById(id);
  }

  @Override
  public List<Campaign> getAllCampaigns() {
    return campaignRepository.findAll();
  }

  @Override
  public Campaign getCampaignById(Long id) {
    return getCampaignOrThrow(id);
  }

  private Campaign getCampaignOrThrow(Long id) {
    return campaignRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + id));
  }

  private EmeraldAccount getAccountOrThrow(Long id) {
    return emeraldAccountRepository
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException("Emerald Account not found with ID: " + id));
  }

  private Product getProductOrThrow(Long id) {
    return productRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
  }

  private Seller getSellerOrThrow(Long id) {
    return sellerRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
  }
}
