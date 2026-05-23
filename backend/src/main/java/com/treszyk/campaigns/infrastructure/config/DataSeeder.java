package com.treszyk.campaigns.infrastructure.config;

import com.treszyk.campaigns.infrastructure.entity.EmeraldAccountJpaEntity;
import com.treszyk.campaigns.infrastructure.entity.ProductJpaEntity;
import com.treszyk.campaigns.infrastructure.entity.SellerJpaEntity;
import com.treszyk.campaigns.infrastructure.jpa.EmeraldAccountJpaRepository;
import com.treszyk.campaigns.infrastructure.jpa.ProductJpaRepository;
import com.treszyk.campaigns.infrastructure.jpa.SellerJpaRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

  private final SellerJpaRepository sellerJpaRepository;
  private final EmeraldAccountJpaRepository emeraldAccountJpaRepository;
  private final ProductJpaRepository productJpaRepository;

  @Override
  public void run(String... args) {
    if (sellerJpaRepository.count() > 0) {
      return;
    }

    SellerJpaEntity alice = sellerJpaRepository.save(new SellerJpaEntity(null, "Alice", "Smith"));
    SellerJpaEntity bob = sellerJpaRepository.save(new SellerJpaEntity(null, "Bob", "Jones"));

    emeraldAccountJpaRepository.saveAll(
        List.of(
            new EmeraldAccountJpaEntity(
                null, "Primary Emeralds", BigDecimal.valueOf(8000.0), alice.getId()),
            new EmeraldAccountJpaEntity(
                null, "Promo Emeralds", BigDecimal.valueOf(2000.0), alice.getId()),
            new EmeraldAccountJpaEntity(
                null, "Bob's Vault", BigDecimal.valueOf(5000.0), bob.getId()),
            new EmeraldAccountJpaEntity(
                null, "Bob's Savings", BigDecimal.valueOf(1500.0), bob.getId())));

    productJpaRepository.saveAll(
        List.of(
            new ProductJpaEntity(
                null,
                "Premium Wireless Headphones",
                "High-fidelity sound, noise cancelling",
                alice.getId()),
            new ProductJpaEntity(
                null, "Ergonomic Office Chair", "Mesh back, lumbar support", alice.getId()),
            new ProductJpaEntity(
                null, "Smart Bluetooth Speaker", "360-degree sound, waterproof", alice.getId()),
            new ProductJpaEntity(
                null,
                "Mechanical Gaming Keyboard",
                "RGB backlighting, tactile switches",
                alice.getId()),
            new ProductJpaEntity(
                null, "Ultra Slim USB-C Hub", "6-in-1 multi-port adapter", bob.getId()),
            new ProductJpaEntity(
                null, "Noise Isolating Earbuds", "Deep bass, secure fit", bob.getId()),
            new ProductJpaEntity(
                null, "Wireless Charging Pad", "15W fast charge, sleek design", bob.getId())));
  }
}
