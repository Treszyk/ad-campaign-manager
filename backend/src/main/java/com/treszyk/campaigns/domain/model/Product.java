package com.treszyk.campaigns.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
  private Long id;
  @NonNull private String name;
  @NonNull private String description;

  // one seller is able to hold many Products
  @NonNull private Long sellerId;
}
