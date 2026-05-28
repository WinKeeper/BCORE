package ru.mentee.power.crm.model;

import java.util.Objects;

public record Lead(
    String id,
    String email,
    String phone,
    String company,
    String status
) {
  // Record автоматически генерирует equals/hashCode по всем полям
  // HashMap сможет использовать id как ключ
  // Пока осуществляем сравнение только по id
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