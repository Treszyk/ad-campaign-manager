package com.treszyk.campaigns.domain.repository;

import com.treszyk.campaigns.domain.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
  // for the demo I've decided to not make a full Product CRUD, I'll pre-seed a couple
  Optional<Product> findById(Long id);

  List<Product> findBySellerId(Long sellerId);

  List<Product> findAll();
}
