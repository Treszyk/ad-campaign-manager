package com.treszyk.campaigns.application.usecase;

import com.treszyk.campaigns.domain.model.Campaign;
import java.util.List;

public interface GetCampaignsUseCase {
  List<Campaign> getAllCampaigns();

  Campaign getCampaignById(Long id);
}
