package com.treszyk.campaigns.infrastructure.jpa;

import com.treszyk.campaigns.infrastructure.entity.ProductJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {
  List<ProductJpaEntity> findBySellerId(Long sellerId);
}
