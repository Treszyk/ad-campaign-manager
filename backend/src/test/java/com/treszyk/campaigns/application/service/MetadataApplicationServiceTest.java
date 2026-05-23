package com.treszyk.campaigns.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.treszyk.campaigns.domain.exception.ResourceNotFoundException;
import com.treszyk.campaigns.domain.model.AdTheme;
import com.treszyk.campaigns.domain.model.Product;
import com.treszyk.campaigns.domain.model.Seller;
import com.treszyk.campaigns.domain.repository.ProductRepository;
import com.treszyk.campaigns.domain.repository.SellerRepository;
import com.treszyk.campaigns.domain.service.CampaignDomainService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MetadataApplicationServiceTest {

  @Mock private ProductRepository productRepository;
  @Mock private SellerRepository sellerRepository;

  @InjectMocks private MetadataApplicationService service;

  @Test
  void getAvailableThemes_ReturnsAllAdThemes() {
    List<AdTheme> result = service.getAvailableThemes();

    assertArrayEquals(AdTheme.values(), result.toArray());
  }

  @Test
  void getKeywords_ReturnsAllowedKeywordsFromDomain() {
    List<String> result = service.getKeywords();

    assertEquals(CampaignDomainService.ALLOWED_KEYWORDS, result);
  }

  @Test
  void getTowns_ReturnsAllowedTownsFromDomain() {
    List<String> result = service.getTowns();

    assertEquals(CampaignDomainService.ALLOWED_TOWNS, result);
  }

  @Test
  void getProductsBySellerId_ReturnsProducts_WhenSellerExists() {
    List<Product> mockProducts =
        List.of(
            new Product(1L, "Laptop", "High-end specs", 1L),
            new Product(2L, "Mouse", "Ergonomic design", 1L));

    when(sellerRepository.findById(1L)).thenReturn(Optional.of(new Seller(1L, "Alice", "Smith")));
    when(productRepository.findBySellerId(1L)).thenReturn(mockProducts);

    List<Product> result = service.getProductsBySellerId(1L);

    assertEquals(2, result.size());
    assertEquals(mockProducts, result);
  }

  @Test
  void getProductsBySellerId_ThrowsResourceNotFoundException_WhenSellerDoesNotExist() {
    when(sellerRepository.findById(1L)).thenReturn(Optional.empty());

    ResourceNotFoundException exception =
        assertThrows(ResourceNotFoundException.class, () -> service.getProductsBySellerId(1L));
    assertTrue(exception.getMessage().contains("Seller with ID 1 not found"));
  }
}
