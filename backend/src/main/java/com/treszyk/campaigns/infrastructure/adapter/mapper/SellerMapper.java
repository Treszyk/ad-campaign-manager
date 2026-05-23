package com.treszyk.campaigns.infrastructure.adapter.mapper;

import com.treszyk.campaigns.domain.model.Seller;
import com.treszyk.campaigns.infrastructure.entity.SellerJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SellerMapper {

  Seller toDomain(SellerJpaEntity entity);

  SellerJpaEntity toEntity(Seller domain);
}
