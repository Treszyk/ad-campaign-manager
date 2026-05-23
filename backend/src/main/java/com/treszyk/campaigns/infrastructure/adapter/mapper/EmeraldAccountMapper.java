package com.treszyk.campaigns.infrastructure.adapter.mapper;

import com.treszyk.campaigns.domain.model.EmeraldAccount;
import com.treszyk.campaigns.infrastructure.entity.EmeraldAccountJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmeraldAccountMapper {

  EmeraldAccount toDomain(EmeraldAccountJpaEntity entity);

  EmeraldAccountJpaEntity toEntity(EmeraldAccount domain);
}
