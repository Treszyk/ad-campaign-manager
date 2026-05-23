package com.treszyk.campaigns.infrastructure.adapter.mapper;

import com.treszyk.campaigns.domain.model.Campaign;
import com.treszyk.campaigns.infrastructure.entity.CampaignJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CampaignMapper {

  Campaign toDomain(CampaignJpaEntity entity);

  CampaignJpaEntity toEntity(Campaign domain);
}
