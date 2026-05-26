package ru.mentee.power.crm.domain;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public record Lead(UUID id, Contact contact, String company, String status) {
  private static final Set<String> ALLOWED_STATUSES = Set.of("NEW", "QUALIFIED", "CONVERTED");

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
    if (!ALLOWED_STATUSES.contains(status)) {
      throw new IllegalArgumentException(
          "Invalid status: " + status + ". Allowed: " + ALLOWED_STATUSES);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Lead other = (Lead) o;
    return Objects.equals(this.id, other.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
