package ru.mentee.power.crm.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Lead;

class LeadStorageTest {

  @Test
  void shouldAddLeadWhenLeadIsUnique() {
    // Given
    LeadStorage storage = new LeadStorage();
    UUID id = UUID.randomUUID();
    Lead uniqueLead = new Lead(id, "ivan@mail.ru", "+7123", "TechCorp", "NEW");

    // When
    boolean added = storage.add(uniqueLead);

    // Then
    assertThat(added).isTrue();
    assertThat(storage.size()).isEqualTo(1);
    assertThat(storage.findAll()).containsExactly(uniqueLead);
  }

  @Test
  void shouldRejectDuplicateWhenEmailAlreadyExists() {
    // Given
    LeadStorage storage = new LeadStorage();
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    Lead existingLead = new Lead(id1, "ivan@mail.ru", "+7123", "TechCorp", "NEW");
    Lead duplicateLead = new Lead(id2, "ivan@mail.ru", "+7456", "Other", "NEW");
    storage.add(existingLead);

    // When
    boolean added = storage.add(duplicateLead);

    // Then
    assertThat(added).isFalse();
    assertThat(storage.size()).isEqualTo(1);
    assertThat(storage.findAll()).containsExactly(existingLead);
  }

  @Test
  void shouldThrowExceptionWhenStorageIsFull() {
    // Given: Заполни хранилище 100 лидами
    LeadStorage storage = new LeadStorage();
    for (int index = 0; index < 100; index++) {
      storage.add(new Lead(UUID.randomUUID(), "lead" + index + "@mail.ru",
          "+7000", "Company", "NEW"));
    }

    // When + Then: 101-й лид должен выбросить исключение
    Lead hundredFirstLead = new Lead(UUID.randomUUID(), "lead101@mail.ru",
        "+7001", "Company", "NEW");

    assertThatThrownBy(() -> storage.add(hundredFirstLead))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Storage is full");
  }

  @Test
  void shouldReturnOnlyAddedLeadsWhenFindAllCalled() {
    // Given
    LeadStorage storage = new LeadStorage();
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    Lead firstLead = new Lead(id1, "ivan@mail.ru", "+7123", "TechCorp", "NEW");
    Lead secondLead = new Lead(id2, "maria@startup.io", "+7456", "StartupLab", "NEW");
    storage.add(firstLead);
    storage.add(secondLead);

    // When
    Lead[] result = storage.findAll();

    // Then
    assertThat(result).hasSize(2);
    assertThat(result).containsExactly(firstLead, secondLead);
  }

  @Test
  void shouldReturnLeadWhenFindByIdWithExistingId() {
    // Given
    LeadStorage storage = new LeadStorage();
    UUID id = UUID.randomUUID();
    Lead lead = new Lead(id, "ivan@mail.ru", "+7123", "TechCorp", "NEW");
    storage.add(lead);

    // When
    Lead found = storage.findById(id);

    // Then
    assertThat(found).isEqualTo(lead);
  }

  @Test
  void shouldReturnNullWhenFindByIdWithNonExistentId() {
    // Given
    LeadStorage storage = new LeadStorage();
    storage.add(new Lead(UUID.randomUUID(), "ivan@mail.ru", "+7123", "TechCorp", "NEW"));

    // When
    Lead found = storage.findById(UUID.randomUUID());

    // Then
    assertThat(found).isNull();
  }
}