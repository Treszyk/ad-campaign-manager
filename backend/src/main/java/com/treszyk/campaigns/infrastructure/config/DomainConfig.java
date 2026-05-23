package com.treszyk.campaigns.infrastructure.config;

import com.treszyk.campaigns.domain.repository.CampaignRepository;
import com.treszyk.campaigns.domain.service.CampaignDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

  @Bean
  public CampaignDomainService campaignDomainService(CampaignRepository campaignRepository) {
    return new CampaignDomainService(campaignRepository);
  }
}
