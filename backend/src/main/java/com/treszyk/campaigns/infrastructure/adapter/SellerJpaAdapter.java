package com.treszyk.campaigns.infrastructure.adapter;

import com.treszyk.campaigns.domain.model.Seller;
import com.treszyk.campaigns.domain.repository.SellerRepository;
import com.treszyk.campaigns.infrastructure.adapter.mapper.SellerMapper;
import com.treszyk.campaigns.infrastructure.jpa.SellerJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SellerJpaAdapter implements SellerRepository {

  private final SellerJpaRepository sellerJpaRepository;
  private final SellerMapper sellerMapper;

  @Override
  public Optional<Seller> findById(Long id) {
    return sellerJpaRepository.findById(id).map(sellerMapper::toDomain);
  }

  @Override
  public List<Seller> findAll() {
    return sellerJpaRepository.findAll().stream().map(sellerMapper::toDomain).toList();
  }
}
