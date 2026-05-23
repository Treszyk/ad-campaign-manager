package com.treszyk.campaigns.infrastructure.jpa;

import com.treszyk.campaigns.infrastructure.entity.SellerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerJpaRepository extends JpaRepository<SellerJpaEntity, Long> {}
