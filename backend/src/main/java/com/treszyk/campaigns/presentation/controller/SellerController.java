package com.treszyk.campaigns.presentation.controller;

import com.treszyk.campaigns.application.usecase.GetSellersUseCase;
import com.treszyk.campaigns.domain.model.Seller;
import com.treszyk.campaigns.presentation.dto.EmeraldAccountResponse;
import com.treszyk.campaigns.presentation.dto.ProductResponse;
import com.treszyk.campaigns.presentation.dto.SellerResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerController {

  private final GetSellersUseCase sellersUseCase;

  @GetMapping
  public ResponseEntity<List<SellerResponse>> getAllSellers() {
    List<SellerResponse> sellers =
        sellersUseCase.getAllSellers().stream()
            .map(s -> new SellerResponse(s.getId(), s.getFirstName(), s.getLastName()))
            .toList();
    return ResponseEntity.ok(sellers);
  }

  @GetMapping("/{id}")
  public ResponseEntity<SellerResponse> getSellerById(@PathVariable Long id) {
    Seller s = sellersUseCase.getSellerById(id);
    return ResponseEntity.ok(new SellerResponse(s.getId(), s.getFirstName(), s.getLastName()));
  }

  @GetMapping("/{id}/accounts")
  public ResponseEntity<List<EmeraldAccountResponse>> getAccountsBySellerId(@PathVariable Long id) {
    List<EmeraldAccountResponse> accounts =
        sellersUseCase.getAccountsBySellerId(id).stream()
            .map(
                a ->
                    new EmeraldAccountResponse(
                        a.getId(), a.getAccountName(), a.getBalance(), a.getSellerId()))
            .toList();
    return ResponseEntity.ok(accounts);
  }

  @GetMapping("/{id}/products")
  public ResponseEntity<List<ProductResponse>> getProductsBySellerId(@PathVariable Long id) {
    List<ProductResponse> products =
        sellersUseCase.getProductsBySellerId(id).stream()
            .map(
                p ->
                    new ProductResponse(
                        p.getId(), p.getName(), p.getDescription(), p.getSellerId()))
            .toList();
    return ResponseEntity.ok(products);
  }
}
