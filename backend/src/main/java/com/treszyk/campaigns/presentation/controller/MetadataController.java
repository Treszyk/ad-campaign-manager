package com.treszyk.campaigns.presentation.controller;

import com.treszyk.campaigns.application.usecase.GetMetadataUseCase;
import com.treszyk.campaigns.domain.model.AdTheme;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metadata")
@RequiredArgsConstructor
public class MetadataController {

  private final GetMetadataUseCase metadataUseCase;

  @GetMapping("/themes")
  public ResponseEntity<List<AdTheme>> getThemes() {
    return ResponseEntity.ok(metadataUseCase.getAvailableThemes());
  }

  @GetMapping("/keywords")
  public ResponseEntity<List<String>> getKeywords() {
    return ResponseEntity.ok(metadataUseCase.getKeywords());
  }

  @GetMapping("/towns")
  public ResponseEntity<List<String>> getTowns() {
    return ResponseEntity.ok(metadataUseCase.getTowns());
  }
}
