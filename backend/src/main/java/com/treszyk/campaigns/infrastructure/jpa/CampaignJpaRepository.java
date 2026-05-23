package com.treszyk.campaigns.infrastructure.jpa;

import com.treszyk.campaigns.infrastructure.entity.CampaignJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignJpaRepository extends JpaRepository<CampaignJpaEntity, Long> {
  boolean existsByProductId(Long productId);

  @Query(
      "SELECT DISTINCT c FROM CampaignJpaEntity c LEFT JOIN FETCH c.keywords WHERE c.sellerId = :sellerId")
  List<CampaignJpaEntity> findBySellerId(@Param("sellerId") Long sellerId);

  @Query("SELECT DISTINCT c FROM CampaignJpaEntity c LEFT JOIN FETCH c.keywords")
  List<CampaignJpaEntity> findAllWithKeywords();
}
