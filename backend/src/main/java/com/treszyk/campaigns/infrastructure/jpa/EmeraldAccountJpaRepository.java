package com.treszyk.campaigns.infrastructure.jpa;

import com.treszyk.campaigns.infrastructure.entity.EmeraldAccountJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmeraldAccountJpaRepository extends JpaRepository<EmeraldAccountJpaEntity, Long> {
  List<EmeraldAccountJpaEntity> findBySellerId(Long sellerId);
}
