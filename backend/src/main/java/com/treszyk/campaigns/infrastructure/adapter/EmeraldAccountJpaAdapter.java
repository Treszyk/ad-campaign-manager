package com.treszyk.campaigns.infrastructure.adapter;

import com.treszyk.campaigns.domain.model.EmeraldAccount;
import com.treszyk.campaigns.domain.repository.EmeraldAccountRepository;
import com.treszyk.campaigns.infrastructure.adapter.mapper.EmeraldAccountMapper;
import com.treszyk.campaigns.infrastructure.entity.EmeraldAccountJpaEntity;
import com.treszyk.campaigns.infrastructure.jpa.EmeraldAccountJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmeraldAccountJpaAdapter implements EmeraldAccountRepository {

  private final EmeraldAccountJpaRepository emeraldAccountJpaRepository;
  private final EmeraldAccountMapper emeraldAccountMapper;

  @Override
  public EmeraldAccount save(EmeraldAccount account) {
    EmeraldAccountJpaEntity entity = emeraldAccountMapper.toEntity(account);
    EmeraldAccountJpaEntity savedEntity = emeraldAccountJpaRepository.save(entity);
    return emeraldAccountMapper.toDomain(savedEntity);
  }

  @Override
  public Optional<EmeraldAccount> findById(Long id) {
    return emeraldAccountJpaRepository.findById(id).map(emeraldAccountMapper::toDomain);
  }

  @Override
  public List<EmeraldAccount> findBySellerId(Long sellerId) {
    return emeraldAccountJpaRepository.findBySellerId(sellerId).stream()
        .map(emeraldAccountMapper::toDomain)
        .toList();
  }
}
