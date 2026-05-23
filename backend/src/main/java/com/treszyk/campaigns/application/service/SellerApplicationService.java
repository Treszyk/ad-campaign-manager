package com.treszyk.campaigns.application.service;

import com.treszyk.campaigns.application.usecase.GetSellersUseCase;
import com.treszyk.campaigns.domain.exception.ResourceNotFoundException;
import com.treszyk.campaigns.domain.model.EmeraldAccount;
import com.treszyk.campaigns.domain.model.Product;
import com.treszyk.campaigns.domain.model.Seller;
import com.treszyk.campaigns.domain.repository.EmeraldAccountRepository;
import com.treszyk.campaigns.domain.repository.ProductRepository;
import com.treszyk.campaigns.domain.repository.SellerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// for the demo I've decided to just expose all sellers and their wallets directly.
// this makes it super easy to switch user profiles on the frontend.
// obviously in a real app you'd get the authenticated user from the security context/token and
// never expose such methods.
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerApplicationService implements GetSellersUseCase {

  private final SellerRepository sellerRepository;
  private final EmeraldAccountRepository emeraldAccountRepository;
  private final ProductRepository productRepository;

  @Override
  public List<Seller> getAllSellers() {
    return sellerRepository.findAll();
  }

  @Override
  public Seller getSellerById(Long id) {
    return sellerRepository
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException("Seller with ID %d not found".formatted(id)));
  }

  @Override
  public List<EmeraldAccount> getAccountsBySellerId(Long sellerId) {
    if (sellerRepository.findById(sellerId).isEmpty()) {
      throw new ResourceNotFoundException("Seller with ID %d not found".formatted(sellerId));
    }
    return emeraldAccountRepository.findBySellerId(sellerId);
  }

  @Override
  public List<Product> getProductsBySellerId(Long sellerId) {
    if (sellerRepository.findById(sellerId).isEmpty()) {
      throw new ResourceNotFoundException("Seller with ID %d not found".formatted(sellerId));
    }
    return productRepository.findBySellerId(sellerId);
  }
}
