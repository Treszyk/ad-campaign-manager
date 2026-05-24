package com.treszyk.campaigns.infrastructure.config;

import com.treszyk.campaigns.domain.model.AdTheme;
import com.treszyk.campaigns.infrastructure.entity.CampaignJpaEntity;
import com.treszyk.campaigns.infrastructure.entity.EmeraldAccountJpaEntity;
import com.treszyk.campaigns.infrastructure.entity.ProductJpaEntity;
import com.treszyk.campaigns.infrastructure.entity.SellerJpaEntity;
import com.treszyk.campaigns.infrastructure.jpa.CampaignJpaRepository;
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
  private final CampaignJpaRepository campaignJpaRepository;

  @Override
  public void run(String... args) {
    if (sellerJpaRepository.count() > 0) {
      return;
    }

    SellerJpaEntity alice = sellerJpaRepository.save(new SellerJpaEntity(null, "Alice", "Smith"));
    SellerJpaEntity bob = sellerJpaRepository.save(new SellerJpaEntity(null, "Bob", "Jones"));

    List<EmeraldAccountJpaEntity> aliceAccounts = List.of(
        new EmeraldAccountJpaEntity(
            null, "Primary Emeralds", BigDecimal.valueOf(8000.0), alice.getId()),
        new EmeraldAccountJpaEntity(
            null, "Promo Emeralds", BigDecimal.valueOf(2000.0), alice.getId()));

    List<EmeraldAccountJpaEntity> savedAliceAccounts = emeraldAccountJpaRepository.saveAll(aliceAccounts);

    List<EmeraldAccountJpaEntity> bobAccounts = List.of(
        new EmeraldAccountJpaEntity(
            null, "Bob's Vault", BigDecimal.valueOf(5000.0), bob.getId()),
        new EmeraldAccountJpaEntity(
            null, "Bob's Savings", BigDecimal.valueOf(1500.0), bob.getId()));

    List<EmeraldAccountJpaEntity> savedBobAccounts = emeraldAccountJpaRepository.saveAll(bobAccounts);

    List<ProductJpaEntity> aliceProducts = List.of(
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
            alice.getId()));

    List<ProductJpaEntity> savedAliceProducts = productJpaRepository.saveAll(aliceProducts);

    List<ProductJpaEntity> bobProducts = List.of(
        new ProductJpaEntity(
            null, "Ultra Slim USB-C Hub", "6-in-1 multi-port adapter", bob.getId()),
        new ProductJpaEntity(
            null, "Noise Isolating Earbuds", "Deep bass, secure fit", bob.getId()),
        new ProductJpaEntity(
            null, "Wireless Charging Pad", "15W fast charge, sleek design", bob.getId()));

    List<ProductJpaEntity> savedBobProducts = productJpaRepository.saveAll(bobProducts);

    BigDecimal aliceCampaignFund = BigDecimal.valueOf(500.00);
    BigDecimal bobCampaignFund = BigDecimal.valueOf(350.00);

    CampaignJpaEntity aliceCampaign = new CampaignJpaEntity(
        null,
        "Premium Sound Launch",
        List.of("headphones", "wireless", "audio"),
        BigDecimal.valueOf(1.50),
        aliceCampaignFund,
        true,
        "Warszawa",
        15.0,
        AdTheme.PASTEL_MINT,
        alice.getId(),
        savedAliceProducts.get(0).getId(),
        savedAliceAccounts.get(0).getId()
    );

    CampaignJpaEntity bobCampaign = new CampaignJpaEntity(
        null,
        "Sleek Hub Promotion",
        List.of("usb-c", "adapter", "tech"),
        BigDecimal.valueOf(2.00),
        bobCampaignFund,
        true,
        "Kraków",
        10.0,
        AdTheme.PASTEL_BLUE,
        bob.getId(),
        savedBobProducts.get(0).getId(),
        savedBobAccounts.get(0).getId()
    );

    campaignJpaRepository.saveAll(List.of(aliceCampaign, bobCampaign));

    EmeraldAccountJpaEntity alicePrimaryAccount = savedAliceAccounts.get(0);
    alicePrimaryAccount.setBalance(alicePrimaryAccount.getBalance().subtract(aliceCampaignFund));
    emeraldAccountJpaRepository.save(alicePrimaryAccount);

    EmeraldAccountJpaEntity bobPrimaryAccount = savedBobAccounts.get(0);
    bobPrimaryAccount.setBalance(bobPrimaryAccount.getBalance().subtract(bobCampaignFund));
    emeraldAccountJpaRepository.save(bobPrimaryAccount);
  }
}
