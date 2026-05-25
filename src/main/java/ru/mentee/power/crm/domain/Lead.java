package ru.mentee.power.crm.domain;

import java.util.UUID;

public record Lead(UUID id, Contact contact, String company, String status) {
  public Lead {
    if (id == null) {
      throw new IllegalArgumentException("Id must not be null");
    }
    if (contact == null) {
      throw new IllegalArgumentException("Contact must not be null");
    }
    if (company == null || company.isBlank()) {
      throw new IllegalArgumentException("Company must not be null or blank");
    }
    if (status == null || status.isBlank()) {
      throw new IllegalArgumentException("Status must not be null or blank");
    }
  }
}
