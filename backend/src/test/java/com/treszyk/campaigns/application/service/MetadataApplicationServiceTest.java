package com.treszyk.campaigns.application.service;

import static org.junit.jupiter.api.Assertions.*;

import com.treszyk.campaigns.domain.model.AdTheme;
import com.treszyk.campaigns.domain.service.CampaignDomainService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MetadataApplicationServiceTest {

  @InjectMocks private MetadataApplicationService service;

  @Test
  void getAvailableThemes_ReturnsAllAdThemes() {
    List<AdTheme> result = service.getAvailableThemes();

    assertArrayEquals(AdTheme.values(), result.toArray());
  }

  @Test
  void getKeywords_ReturnsAllowedKeywordsFromDomain() {
    List<String> result = service.getKeywords();

    assertEquals(CampaignDomainService.ALLOWED_KEYWORDS, result);
  }

  @Test
  void getTowns_ReturnsAllowedTownsFromDomain() {
    List<String> result = service.getTowns();

    assertEquals(CampaignDomainService.ALLOWED_TOWNS, result);
  }
}
