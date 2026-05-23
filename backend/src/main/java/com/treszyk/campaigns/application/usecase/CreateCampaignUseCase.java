package com.treszyk.campaigns.application.usecase;

import com.treszyk.campaigns.application.dto.CreateCampaignCommand;
import com.treszyk.campaigns.domain.model.Campaign;

public interface CreateCampaignUseCase {
  Campaign createCampaign(CreateCampaignCommand command);
}
