package com.treszyk.campaigns.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seller {
  private Long id;
  @NonNull private String firstName;
  @NonNull private String lastName;
}
