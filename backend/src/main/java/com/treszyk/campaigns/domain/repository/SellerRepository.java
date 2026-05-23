package com.treszyk.campaigns.domain.repository;

import com.treszyk.campaigns.domain.model.Seller;
import java.util.List;
import java.util.Optional;

public interface SellerRepository {
  // for the demo I've decided to not make a full Seller CRUD, I'll pre-seed a couple
  Optional<Seller> findById(Long id);

  List<Seller> findAll();
}
