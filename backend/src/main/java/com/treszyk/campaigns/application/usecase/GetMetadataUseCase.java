package com.treszyk.campaigns.application.usecase;

import com.treszyk.campaigns.domain.model.AdTheme;
import com.treszyk.campaigns.domain.model.Product;
import java.util.List;

public interface GetMetadataUseCase {
  List<AdTheme> getAvailableThemes();

  List<String> getKeywords();

  List<String> getTowns();

  List<Product> getProductsBySellerId(Long sellerId);
}
