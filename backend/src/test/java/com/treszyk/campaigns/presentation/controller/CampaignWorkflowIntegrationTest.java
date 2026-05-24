package com.treszyk.campaigns.presentation.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.treszyk.campaigns.domain.model.AdTheme;
import com.treszyk.campaigns.presentation.dto.CampaignResponse;
import com.treszyk.campaigns.presentation.dto.CreateCampaignRequest;
import com.treszyk.campaigns.presentation.dto.EmeraldAccountResponse;
import com.treszyk.campaigns.presentation.dto.ProductResponse;
import com.treszyk.campaigns.presentation.dto.SellerResponse;
import com.treszyk.campaigns.presentation.dto.UpdateCampaignRequest;
import com.treszyk.campaigns.presentation.exception.GlobalExceptionHandler.ErrorResponse;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CampaignWorkflowIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void campaignLifecycle_ExecutesFullLifecycleAndValidatesDomainRules() throws Exception {
    MvcResult sellersResult =
        mockMvc.perform(get("/api/sellers")).andExpect(status().isOk()).andReturn();

    List<SellerResponse> sellers =
        objectMapper.readValue(
            sellersResult.getResponse().getContentAsString(),
            new TypeReference<List<SellerResponse>>() {});

    assertFalse(sellers.isEmpty());
    SellerResponse alice =
        sellers.stream().filter(s -> "Alice".equals(s.firstName())).findFirst().orElseThrow();

    MvcResult accountsResult =
        mockMvc
            .perform(get("/api/sellers/" + alice.id() + "/accounts"))
            .andExpect(status().isOk())
            .andReturn();

    List<EmeraldAccountResponse> accounts =
        objectMapper.readValue(
            accountsResult.getResponse().getContentAsString(),
            new TypeReference<List<EmeraldAccountResponse>>() {});

    EmeraldAccountResponse primaryWallet =
        accounts.stream()
            .filter(a -> "Primary Emeralds".equals(a.accountName()))
            .findFirst()
            .orElseThrow();

    assertEquals(0, new BigDecimal("7500.00").compareTo(primaryWallet.balance()));

    MvcResult productsResult =
        mockMvc
            .perform(get("/api/sellers/" + alice.id() + "/products"))
            .andExpect(status().isOk())
            .andReturn();

    List<ProductResponse> products =
        objectMapper.readValue(
            productsResult.getResponse().getContentAsString(),
            new TypeReference<List<ProductResponse>>() {});

    assertFalse(products.isEmpty());
    ProductResponse chair =
        products.stream()
            .filter(p -> "Ergonomic Office Chair".equals(p.name()))
            .findFirst()
            .orElseThrow();

    CreateCampaignRequest createReq =
        new CreateCampaignRequest(
            "Premium Wireless Promotion",
            List.of("deal", "exclusive"),
            new BigDecimal("5.50"),
            new BigDecimal("3000.00"),
            true,
            "Warszawa",
            15.0,
            AdTheme.PASTEL_MINT,
            alice.id(),
            chair.id(),
            primaryWallet.id());

    MvcResult createResult =
        mockMvc
            .perform(
                post("/api/campaigns")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createReq)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andReturn();

    CampaignResponse createdCampaign =
        objectMapper.readValue(
            createResult.getResponse().getContentAsString(), CampaignResponse.class);

    assertNotNull(createdCampaign.id());
    assertEquals("Premium Wireless Promotion", createdCampaign.name());
    assertEquals(0, new BigDecimal("3000.00").compareTo(createdCampaign.campaignFund()));

    String locationHeader = createResult.getResponse().getHeader("Location");
    assertNotNull(locationHeader);
    assertTrue(locationHeader.contains("/api/campaigns/" + createdCampaign.id()));

    MvcResult accountsPostCreateResult =
        mockMvc
            .perform(get("/api/sellers/" + alice.id() + "/accounts"))
            .andExpect(status().isOk())
            .andReturn();

    List<EmeraldAccountResponse> accountsPostCreate =
        objectMapper.readValue(
            accountsPostCreateResult.getResponse().getContentAsString(),
            new TypeReference<List<EmeraldAccountResponse>>() {});

    EmeraldAccountResponse primaryWalletPostCreate =
        accountsPostCreate.stream()
            .filter(a -> primaryWallet.id().equals(a.id()))
            .findFirst()
            .orElseThrow();

    assertEquals(0, new BigDecimal("4500.00").compareTo(primaryWalletPostCreate.balance()));

    MvcResult getResult =
        mockMvc
            .perform(get("/api/campaigns/" + createdCampaign.id()))
            .andExpect(status().isOk())
            .andReturn();

    CampaignResponse fetchedCampaign =
        objectMapper.readValue(
            getResult.getResponse().getContentAsString(), CampaignResponse.class);

    assertEquals(createdCampaign.id(), fetchedCampaign.id());
    assertEquals("Premium Wireless Promotion", fetchedCampaign.name());

    UpdateCampaignRequest updateReq =
        new UpdateCampaignRequest(
            "Premium Wireless Promotion Updated",
            List.of("deal", "exclusive", "new"),
            new BigDecimal("6.00"),
            new BigDecimal("2000.00"),
            true,
            "Kraków",
            20.0,
            AdTheme.PASTEL_CORAL);

    MvcResult updateResult =
        mockMvc
            .perform(
                put("/api/campaigns/" + createdCampaign.id())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateReq)))
            .andExpect(status().isOk())
            .andReturn();

    CampaignResponse updatedCampaign =
        objectMapper.readValue(
            updateResult.getResponse().getContentAsString(), CampaignResponse.class);

    assertEquals("Premium Wireless Promotion Updated", updatedCampaign.name());
    assertEquals(0, new BigDecimal("2000.00").compareTo(updatedCampaign.campaignFund()));
    assertEquals("Kraków", updatedCampaign.town());

    MvcResult accountsPostUpdateResult =
        mockMvc
            .perform(get("/api/sellers/" + alice.id() + "/accounts"))
            .andExpect(status().isOk())
            .andReturn();

    List<EmeraldAccountResponse> accountsPostUpdate =
        objectMapper.readValue(
            accountsPostUpdateResult.getResponse().getContentAsString(),
            new TypeReference<List<EmeraldAccountResponse>>() {});

    EmeraldAccountResponse primaryWalletPostUpdate =
        accountsPostUpdate.stream()
            .filter(a -> primaryWallet.id().equals(a.id()))
            .findFirst()
            .orElseThrow();

    assertEquals(0, new BigDecimal("5500.00").compareTo(primaryWalletPostUpdate.balance()));

    mockMvc
        .perform(delete("/api/campaigns/" + createdCampaign.id()))
        .andExpect(status().isNoContent());

    MvcResult notFoundResult =
        mockMvc
            .perform(get("/api/campaigns/" + createdCampaign.id()))
            .andExpect(status().isNotFound())
            .andReturn();

    ErrorResponse errorResponse =
        objectMapper.readValue(
            notFoundResult.getResponse().getContentAsString(), ErrorResponse.class);

    assertEquals(404, errorResponse.status());
    assertTrue(errorResponse.message().contains("not found"));
  }

  @Test
  void campaignLifecycle_ReturnsBadRequest_WhenDomainRulesAreViolated() throws Exception {
    CreateCampaignRequest invalidTownReq =
        new CreateCampaignRequest(
            "Invalid Town Promo",
            List.of("deal"),
            new BigDecimal("2.00"),
            new BigDecimal("100.00"),
            true,
            "Gdyniaa",
            10.0,
            AdTheme.PASTEL_MINT,
            1L,
            2L,
            1L);

    MvcResult townResult =
        mockMvc
            .perform(
                post("/api/campaigns")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidTownReq)))
            .andExpect(status().isBadRequest())
            .andReturn();

    ErrorResponse townError =
        objectMapper.readValue(townResult.getResponse().getContentAsString(), ErrorResponse.class);

    assertEquals(400, townError.status());
    assertTrue(townError.message().contains("is not supported"));

    String invalidJson =
        """
        {
          "name": "Invalid Theme Promo",
          "keywords": ["deal"],
          "bidAmount": 2.00,
          "campaignFund": 100.00,
          "status": true,
          "town": "Warszawa",
          "radiusKm": 10.0,
          "adTheme": "LIGHT",
          "sellerId": 1,
          "productId": 2,
          "emeraldAccountId": 1
        }
        """;

    MvcResult themeResult =
        mockMvc
            .perform(
                post("/api/campaigns").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
            .andExpect(status().isBadRequest())
            .andReturn();

    ErrorResponse themeError =
        objectMapper.readValue(themeResult.getResponse().getContentAsString(), ErrorResponse.class);

    assertEquals(400, themeError.status());
    assertTrue(themeError.message().contains("Invalid value provided for field: adTheme"));
  }

  @Test
  void metadata_ReturnsThemesKeywordsAndTowns() throws Exception {
    mockMvc
        .perform(get("/api/metadata/themes"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0]").value("PASTEL_MINT"));

    mockMvc
        .perform(get("/api/metadata/keywords"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0]").value("headphones"));

    mockMvc
        .perform(get("/api/metadata/towns"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0]").value("Warszawa"));
  }

  @Test
  void getCampaignsBySellerId_ReturnsOnlySellerScopedCampaigns() throws Exception {
    MvcResult sellersResult =
        mockMvc.perform(get("/api/sellers")).andExpect(status().isOk()).andReturn();

    List<SellerResponse> sellers =
        objectMapper.readValue(
            sellersResult.getResponse().getContentAsString(),
            new TypeReference<List<SellerResponse>>() {});

    SellerResponse alice =
        sellers.stream().filter(s -> "Alice".equals(s.firstName())).findFirst().orElseThrow();
    SellerResponse bob =
        sellers.stream().filter(s -> "Bob".equals(s.firstName())).findFirst().orElseThrow();

    MvcResult aliceCampaignsResult =
        mockMvc
            .perform(get("/api/campaigns/seller/" + alice.id()))
            .andExpect(status().isOk())
            .andReturn();

    List<CampaignResponse> aliceCampaigns =
        objectMapper.readValue(
            aliceCampaignsResult.getResponse().getContentAsString(),
            new TypeReference<List<CampaignResponse>>() {});

    assertEquals(1, aliceCampaigns.size());
    assertEquals("Premium Sound Launch", aliceCampaigns.get(0).name());
    assertEquals(alice.id(), aliceCampaigns.get(0).sellerId());

    MvcResult bobCampaignsResult =
        mockMvc
            .perform(get("/api/campaigns/seller/" + bob.id()))
            .andExpect(status().isOk())
            .andReturn();

    List<CampaignResponse> bobCampaigns =
        objectMapper.readValue(
            bobCampaignsResult.getResponse().getContentAsString(),
            new TypeReference<List<CampaignResponse>>() {});

    assertEquals(1, bobCampaigns.size());
    assertEquals("Sleek Hub Promotion", bobCampaigns.get(0).name());
    assertEquals(bob.id(), bobCampaigns.get(0).sellerId());
  }
}
