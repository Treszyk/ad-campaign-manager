package com.treszyk.campaigns.infrastructure.adapter;

import com.treszyk.campaigns.domain.model.Product;
import com.treszyk.campaigns.domain.repository.ProductRepository;
import com.treszyk.campaigns.infrastructure.adapter.mapper.ProductMapper;
import com.treszyk.campaigns.infrastructure.jpa.ProductJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductJpaAdapter implements ProductRepository {

  private final ProductJpaRepository productJpaRepository;
  private final ProductMapper productMapper;

  @Override
  public Optional<Product> findById(Long id) {
    return productJpaRepository.findById(id).map(productMapper::toDomain);
  }

  @Override
  public List<Product> findBySellerId(Long sellerId) {
    return productJpaRepository.findBySellerId(sellerId).stream()
        .map(productMapper::toDomain)
        .toList();
  }

  @Override
  public List<Product> findAll() {
    return productJpaRepository.findAll().stream().map(productMapper::toDomain).toList();
  }
}
