package com.treszyk.campaigns.application.usecase;

import com.treszyk.campaigns.domain.model.Seller;
import java.util.List;

public interface GetSellersUseCase {
  List<Seller> getAllSellers();
}
