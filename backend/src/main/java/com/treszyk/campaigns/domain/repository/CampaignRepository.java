package com.treszyk.campaigns.domain.repository;

import com.treszyk.campaigns.domain.model.Campaign;
import java.util.List;
import java.util.Optional;

public interface CampaignRepository {
  Campaign save(Campaign campaign);

  Optional<Campaign> findById(Long id);

  List<Campaign> findAll();

  List<Campaign> findBySellerId(Long sellerId);

  boolean existsByProductId(Long productId);

  void deleteById(Long id);
}
