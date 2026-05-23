package com.treszyk.campaigns.application.service;

import com.treszyk.campaigns.application.usecase.GetMetadataUseCase;
import com.treszyk.campaigns.domain.model.AdTheme;
import com.treszyk.campaigns.domain.service.CampaignDomainService;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MetadataApplicationService implements GetMetadataUseCase {

  @Override
  public List<AdTheme> getAvailableThemes() {
    return Arrays.asList(AdTheme.values());
  }

  @Override
  public List<String> getKeywords() {
    return CampaignDomainService.ALLOWED_KEYWORDS;
  }

  @Override
  public List<String> getTowns() {
    return CampaignDomainService.ALLOWED_TOWNS;
  }
}
