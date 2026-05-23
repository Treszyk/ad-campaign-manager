package com.treszyk.campaigns.application.usecase;

import com.treszyk.campaigns.application.dto.UpdateCampaignCommand;
import com.treszyk.campaigns.domain.model.Campaign;

public interface UpdateCampaignUseCase {
  Campaign updateCampaign(UpdateCampaignCommand command);
}
