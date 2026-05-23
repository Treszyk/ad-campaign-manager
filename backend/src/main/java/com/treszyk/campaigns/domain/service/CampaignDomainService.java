package com.treszyk.campaigns.domain.service;

import com.treszyk.campaigns.domain.exception.InsufficientFundsException;
import com.treszyk.campaigns.domain.exception.ProductAlreadyCampaignedException;
import com.treszyk.campaigns.domain.model.EmeraldAccount;
import com.treszyk.campaigns.domain.model.Product;
import com.treszyk.campaigns.domain.repository.CampaignRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CampaignDomainService {

  public static final List<String> ALLOWED_TOWNS =
      List.of(
          "Warszawa",
          "Kraków",
          "Wrocław",
          "Poznań",
          "Gdańsk",
          "Szczecin",
          "Łódź",
          "Katowice",
          "Lublin",
          "Gdynia",
          "Białystok",
          "Gliwice",
          "Radom",
          "Rzeszów",
          "Tychy",
          "Zielona Góra",
          "Płock",
          "Elbląg",
          "Piotrków Trybunalski",
          "Słupsk",
          "Kalisz",
          "Toruń",
          "Koszalin",
          "Legnica",
          "Olsztyn",
          "Tarnów",
          "Siedlce",
          "Kielce");

  public static final List<String> ALLOWED_KEYWORDS =
      List.of(
          "headphones",
          "wireless",
          "bluetooth",
          "premium",
          "audio",
          "sale",
          "music",
          "ergonomic",
          "office",
          "gadget",
          "deal",
          "discount",
          "smart",
          "tech",
          "new",
          "hot",
          "limited",
          "exclusive");

  private final CampaignRepository campaignRepository;

  public void validateTownAndKeywords(String town, List<String> keywords) {
    if (town != null && !ALLOWED_TOWNS.contains(town)) {
      throw new IllegalArgumentException("Town '%s' is not supported".formatted(town));
    }
    if (keywords != null) {
      keywords.stream()
          .filter(kw -> !ALLOWED_KEYWORDS.contains(kw))
          .findFirst()
          .ifPresent(
              kw -> {
                throw new IllegalArgumentException("Keyword '%s' is not supported".formatted(kw));
              });
    }
  }

  public void validateCampaignCreation(
      @NonNull EmeraldAccount account,
      @NonNull Product product,
      @NonNull Long sellerId,
      @NonNull BigDecimal campaignFund) {
    Long productId = product.getId();

    if (!account.getSellerId().equals(sellerId))
      throw new IllegalArgumentException(
          "Seller with ID %d does not own Emerald Account with ID %d"
              .formatted(sellerId, account.getId()));

    if (!product.getSellerId().equals(sellerId))
      throw new IllegalArgumentException(
          "Seller with ID %d does not own Product with ID %d".formatted(sellerId, productId));

    if (campaignRepository.existsByProductId(productId))
      throw new ProductAlreadyCampaignedException(productId);

    if (account.getBalance().compareTo(campaignFund) < 0)
      throw new InsufficientFundsException(campaignFund, account.getBalance());
  }

  public void deductFunds(@NonNull EmeraldAccount account, @NonNull BigDecimal amount) {
    if (account.getBalance().compareTo(amount) < 0)
      throw new InsufficientFundsException(amount, account.getBalance());

    account.setBalance(account.getBalance().subtract(amount));
  }

  public void refundFunds(@NonNull EmeraldAccount account, @NonNull BigDecimal amount) {
    account.setBalance(account.getBalance().add(amount));
  }

  public void adjustFunds(
      @NonNull EmeraldAccount account, @NonNull BigDecimal oldFund, @NonNull BigDecimal newFund) {
    BigDecimal difference = newFund.subtract(oldFund);
    if (difference.compareTo(BigDecimal.ZERO) >= 0) this.deductFunds(account, difference);
    else this.refundFunds(account, difference.abs());
  }
}
