package com.treszyk.campaigns.domain.repository;

import com.treszyk.campaigns.domain.model.EmeraldAccount;
import java.util.List;
import java.util.Optional;

public interface EmeraldAccountRepository {
  // for the demo I've decided to not make a full EmeraldAccount CRUD, I'll pre-seed a couple
  EmeraldAccount save(EmeraldAccount account);

  Optional<EmeraldAccount> findById(Long id);

  List<EmeraldAccount> findBySellerId(Long sellerId);
}
