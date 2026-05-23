package com.treszyk.campaigns.presentation.controller;

import com.treszyk.campaigns.application.dto.CreateCampaignCommand;
import com.treszyk.campaigns.application.dto.UpdateCampaignCommand;
import com.treszyk.campaigns.application.usecase.CreateCampaignUseCase;
import com.treszyk.campaigns.application.usecase.DeleteCampaignUseCase;
import com.treszyk.campaigns.application.usecase.GetCampaignsUseCase;
import com.treszyk.campaigns.application.usecase.UpdateCampaignUseCase;
import com.treszyk.campaigns.domain.model.Campaign;
import com.treszyk.campaigns.presentation.dto.CampaignResponse;
import com.treszyk.campaigns.presentation.dto.CreateCampaignRequest;
import com.treszyk.campaigns.presentation.dto.UpdateCampaignRequest;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {

  private final CreateCampaignUseCase createCampaignUseCase;
  private final UpdateCampaignUseCase updateCampaignUseCase;
  private final DeleteCampaignUseCase deleteCampaignUseCase;
  private final GetCampaignsUseCase getCampaignsUseCase;

  @PostMapping
  public ResponseEntity<CampaignResponse> createCampaign(@RequestBody CreateCampaignRequest req) {
    CreateCampaignCommand cmd =
        new CreateCampaignCommand(
            req.name(),
            req.keywords(),
            req.bidAmount(),
            req.campaignFund(),
            req.status(),
            req.town(),
            req.radiusKm(),
            req.adTheme(),
            req.sellerId(),
            req.productId(),
            req.emeraldAccountId());

    Campaign created = createCampaignUseCase.createCampaign(cmd);
    CampaignResponse response = mapToResponse(created);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.getId())
            .toUri();

    return ResponseEntity.created(location).body(response);
  }

  @GetMapping
  public ResponseEntity<List<CampaignResponse>> getAllCampaigns() {
    List<CampaignResponse> campaigns =
        getCampaignsUseCase.getAllCampaigns().stream().map(this::mapToResponse).toList();
    return ResponseEntity.ok(campaigns);
  }

  @GetMapping("/seller/{sellerId}")
  public ResponseEntity<List<CampaignResponse>> getCampaignsBySellerId(
      @PathVariable Long sellerId) {
    List<CampaignResponse> campaigns =
        getCampaignsUseCase.getCampaignsBySellerId(sellerId).stream()
            .map(this::mapToResponse)
            .toList();
    return ResponseEntity.ok(campaigns);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CampaignResponse> getCampaignById(@PathVariable Long id) {
    Campaign campaign = getCampaignsUseCase.getCampaignById(id);
    return ResponseEntity.ok(mapToResponse(campaign));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CampaignResponse> updateCampaign(
      @PathVariable Long id, @RequestBody UpdateCampaignRequest req) {
    UpdateCampaignCommand cmd =
        new UpdateCampaignCommand(
            id,
            req.name(),
            req.keywords(),
            req.bidAmount(),
            req.campaignFund(),
            req.status(),
            req.town(),
            req.radiusKm(),
            req.adTheme());

    Campaign updated = updateCampaignUseCase.updateCampaign(cmd);
    return ResponseEntity.ok(mapToResponse(updated));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
    deleteCampaignUseCase.deleteCampaign(id);
    return ResponseEntity.noContent().build();
  }

  private CampaignResponse mapToResponse(Campaign c) {
    return new CampaignResponse(
        c.getId(),
        c.getName(),
        c.getKeywords(),
        c.getBidAmount(),
        c.getCampaignFund(),
        c.getStatus(),
        c.getTown(),
        c.getRadiusKm(),
        c.getAdTheme(),
        c.getSellerId(),
        c.getProductId(),
        c.getEmeraldAccountId());
  }
}
