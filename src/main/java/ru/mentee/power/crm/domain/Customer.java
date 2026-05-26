package ru.mentee.power.crm.domain;

import java.util.UUID;

public record Customer(UUID id, Contact contact, Address billingAddress,
                       String loyaltyTier) {
  public Customer {
    if (id == null) {
      throw new IllegalArgumentException("id must not be null");
    }
    if (contact == null) {
      throw new IllegalArgumentException("Contact must not be null");
    }
    if (billingAddress == null) {
      throw new IllegalArgumentException("BillingAddress must not be null");
    }
    if (loyaltyTier == null || loyaltyTier.isBlank()) {
      throw new IllegalArgumentException("LoyaltyTier must not be null or blank");
    }
  }

}
