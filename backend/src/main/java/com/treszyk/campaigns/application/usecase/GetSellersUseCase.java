package com.treszyk.campaigns.application.usecase;

import com.treszyk.campaigns.domain.model.EmeraldAccount;
import com.treszyk.campaigns.domain.model.Seller;
import java.util.List;

public interface GetSellersUseCase {
  List<Seller> getAllSellers();

  Seller getSellerById(Long id);

  List<EmeraldAccount> getAccountsBySellerId(Long sellerId);
}
