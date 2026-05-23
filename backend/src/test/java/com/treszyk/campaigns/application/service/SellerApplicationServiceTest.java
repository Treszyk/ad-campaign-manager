package com.treszyk.campaigns.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.treszyk.campaigns.domain.exception.ResourceNotFoundException;
import com.treszyk.campaigns.domain.model.EmeraldAccount;
import com.treszyk.campaigns.domain.model.Seller;
import com.treszyk.campaigns.domain.repository.EmeraldAccountRepository;
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

  @InjectMocks private SellerApplicationService service;

  @Test
  void getAllSellers_ReturnsAllSellers_WhenSellersExist() {
    List<Seller> mockSellers =
        List.of(new Seller(1L, "Alice", "Smith"), new Seller(2L, "Bob", "Jones"));
    when(sellerRepository.findAll()).thenReturn(mockSellers);

    List<Seller> result = service.getAllSellers();

    assertThat(result).hasSize(2).isEqualTo(mockSellers);
  }

  @Test
  void getSellerById_ReturnsSeller_WhenSellerExists() {
    Seller mockSeller = new Seller(1L, "Alice", "Smith");
    when(sellerRepository.findById(1L)).thenReturn(Optional.of(mockSeller));

    Seller result = service.getSellerById(1L);

    assertThat(result).isEqualTo(mockSeller);
  }

  @Test
  void getSellerById_ThrowsResourceNotFoundException_WhenSellerDoesNotExist() {
    when(sellerRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.getSellerById(1L))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Seller with ID 1 not found");
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

    assertThat(result).hasSize(2).isEqualTo(mockAccounts);
  }

  @Test
  void getAccountsBySellerId_ThrowsResourceNotFoundException_WhenSellerDoesNotExist() {
    when(sellerRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.getAccountsBySellerId(1L))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Seller with ID 1 not found");
  }
}
