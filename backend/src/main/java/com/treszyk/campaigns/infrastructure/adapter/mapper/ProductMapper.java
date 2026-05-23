package com.treszyk.campaigns.infrastructure.adapter.mapper;

import com.treszyk.campaigns.domain.model.Product;
import com.treszyk.campaigns.infrastructure.entity.ProductJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  Product toDomain(ProductJpaEntity entity);

  ProductJpaEntity toEntity(Product domain);
}
