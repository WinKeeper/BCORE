package ru.mentee.power.crm.model;

import java.util.Objects;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record Lead(
    UUID id,

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    String email,

    @NotBlank(message = "Телефон обязателен")
    @Pattern(regexp = "^\\+?[0-9\\-\\s]{6,20}$", message = "Некорректный номер телефона")
    String phone,

    @NotBlank(message = "Название компании обязательно")
    @Size(min = 2, max = 100)
    String company,

    @NotNull(message = "Указать статус обязательно")
    LeadStatus status
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