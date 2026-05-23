package com.treszyk.campaigns.application.usecase;

import com.treszyk.campaigns.domain.model.AdTheme;
import java.util.List;

public interface GetMetadataUseCase {
  List<AdTheme> getAvailableThemes();
}
