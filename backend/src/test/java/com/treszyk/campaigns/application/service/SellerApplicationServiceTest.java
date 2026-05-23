package com.treszyk.campaigns.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.treszyk.campaigns.domain.exception.ResourceNotFoundException;
import com.treszyk.campaigns.domain.model.EmeraldAccount;
import com.treszyk.campaigns.domain.model.Product;
import com.treszyk.campaigns.domain.model.Seller;
import com.treszyk.campaigns.domain.repository.EmeraldAccountRepository;
import com.treszyk.campaigns.domain.repository.ProductRepository;
import com.treszyk.campaigns.domain.repository.SellerRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SellerApplicationServiceTest {

  @Mock private SellerRepository sellerRepository;
  @Mock private EmeraldAccountRepository emeraldAccountRepository;
  @Mock private ProductRepository productRepository;

  @InjectMocks private SellerApplicationService service;

  @Test
  void getAllSellers_ReturnsAllSellers_WhenSellersExist() {
    List<Seller> mockSellers =
        List.of(new Seller(1L, "Alice", "Smith"), new Seller(2L, "Bob", "Jones"));
    when(sellerRepository.findAll()).thenReturn(mockSellers);

    List<Seller> result = service.getAllSellers();

    assertEquals(2, result.size());
    assertEquals(mockSellers, result);
  }

  @Test
  void getSellerById_ReturnsSeller_WhenSellerExists() {
    Seller mockSeller = new Seller(1L, "Alice", "Smith");
    when(sellerRepository.findById(1L)).thenReturn(Optional.of(mockSeller));

    Seller result = service.getSellerById(1L);

    assertEquals(mockSeller, result);
  }

  @Test
  void getSellerById_ThrowsResourceNotFoundException_WhenSellerDoesNotExist() {
    when(sellerRepository.findById(1L)).thenReturn(Optional.empty());

    ResourceNotFoundException exception =
        assertThrows(ResourceNotFoundException.class, () -> service.getSellerById(1L));
    assertTrue(exception.getMessage().contains("Seller with ID 1 not found"));
  }

  @Test
  void getAccountsBySellerId_ReturnsAccounts_WhenSellerExists() {
    List<EmeraldAccount> mockAccounts =
        List.of(
            new EmeraldAccount(1L, "Primary", BigDecimal.valueOf(100.0), 1L),
            new EmeraldAccount(2L, "Promo", BigDecimal.valueOf(50.0), 1L));

    when(sellerRepository.findById(1L)).thenReturn(Optional.of(new Seller(1L, "Alice", "Smith")));
    when(emeraldAccountRepository.findBySellerId(1L)).thenReturn(mockAccounts);

    List<EmeraldAccount> result = service.getAccountsBySellerId(1L);

    assertEquals(2, result.size());
    assertEquals(mockAccounts, result);
  }

  @Test
  void getAccountsBySellerId_ThrowsResourceNotFoundException_WhenSellerDoesNotExist() {
    when(sellerRepository.findById(1L)).thenReturn(Optional.empty());

    ResourceNotFoundException exception =
        assertThrows(ResourceNotFoundException.class, () -> service.getAccountsBySellerId(1L));
    assertTrue(exception.getMessage().contains("Seller with ID 1 not found"));
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
