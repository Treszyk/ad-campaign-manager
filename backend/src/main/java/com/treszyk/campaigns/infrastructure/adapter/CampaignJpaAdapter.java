package com.treszyk.campaigns.infrastructure.adapter;

import com.treszyk.campaigns.domain.model.Campaign;
import com.treszyk.campaigns.domain.repository.CampaignRepository;
import com.treszyk.campaigns.infrastructure.adapter.mapper.CampaignMapper;
import com.treszyk.campaigns.infrastructure.entity.CampaignJpaEntity;
import com.treszyk.campaigns.infrastructure.jpa.CampaignJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CampaignJpaAdapter implements CampaignRepository {

  private final CampaignJpaRepository campaignJpaRepository;
  private final CampaignMapper campaignMapper;

  @Override
  public Campaign save(Campaign campaign) {
    CampaignJpaEntity entity = campaignMapper.toEntity(campaign);
    CampaignJpaEntity savedEntity = campaignJpaRepository.save(entity);
    return campaignMapper.toDomain(savedEntity);
  }

  @Override
  public Optional<Campaign> findById(Long id) {
    return campaignJpaRepository.findById(id).map(campaignMapper::toDomain);
  }

  @Override
  public List<Campaign> findAll() {
    return campaignJpaRepository.findAllWithKeywords().stream()
        .map(campaignMapper::toDomain)
        .toList();
  }

  @Override
  public List<Campaign> findBySellerId(Long sellerId) {
    return campaignJpaRepository.findBySellerId(sellerId).stream()
        .map(campaignMapper::toDomain)
        .toList();
  }

  @Override
  public boolean existsByProductId(Long productId) {
    return campaignJpaRepository.existsByProductId(productId);
  }

  @Override
  public void deleteById(Long id) {
    campaignJpaRepository.deleteById(id);
  }
}
