package com.treszyk.campaigns.domain.exception;

public class ProductAlreadyCampaignedException extends RuntimeException {
  public ProductAlreadyCampaignedException(Long productId) {
    super(String.format("Product with ID %d already has an active campaign", productId));
  }
}
