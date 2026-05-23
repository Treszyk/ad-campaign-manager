package com.treszyk.campaigns.application.service;

import com.treszyk.campaigns.application.usecase.GetMetadataUseCase;
import com.treszyk.campaigns.domain.exception.ResourceNotFoundException;
import com.treszyk.campaigns.domain.model.AdTheme;
import com.treszyk.campaigns.domain.model.Product;
import com.treszyk.campaigns.domain.repository.ProductRepository;
import com.treszyk.campaigns.domain.repository.SellerRepository;
import com.treszyk.campaigns.domain.service.CampaignDomainService;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MetadataApplicationService implements GetMetadataUseCase {

  private final ProductRepository productRepository;
  private final SellerRepository sellerRepository;

  @Override
  public List<AdTheme> getAvailableThemes() {
    return Arrays.asList(AdTheme.values());
  }

  @Override
  public List<String> getKeywords() {
    return CampaignDomainService.ALLOWED_KEYWORDS;
  }

  @Override
  public List<String> getTowns() {
    return CampaignDomainService.ALLOWED_TOWNS;
  }

  @Override
  public List<Product> getProductsBySellerId(Long sellerId) {
    if (sellerRepository.findById(sellerId).isEmpty()) {
      throw new ResourceNotFoundException("Seller with ID %d not found".formatted(sellerId));
    }
    return productRepository.findBySellerId(sellerId);
  }
}
