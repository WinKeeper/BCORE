package ru.mentee.power.crm.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record Lead(
    UUID id,
    String email,
    String phone,
    String company,
    LeadStatus status,
    LocalDateTime updatedAt
) {
  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Lead lead = (Lead) o;
    return Objects.equals(id, lead.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}