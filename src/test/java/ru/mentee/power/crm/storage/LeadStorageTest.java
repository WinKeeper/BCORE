package ru.mentee.power.crm.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

class LeadStorageTest {

  private static Contact contact(String email, String phone) {
    return new Contact(email, phone, new Address("Moscow", "Tverskaya", "123456"));
  }

  @Test
  @DisplayName("Should add lead when lead is unique")
  void shouldAddLeadWhenLeadIsUnique() {
    // Given
    LeadStorage storage = new LeadStorage();
    UUID id = UUID.randomUUID();
    Lead uniqueLead = new Lead(id, contact("ivan@mail.ru", "+7123"), "TechCorp", "NEW");

    // When
    boolean added = storage.add(uniqueLead);

    // Then
    assertThat(added).isTrue();
    assertThat(storage.size()).isEqualTo(1);
    assertThat(storage.findAll()).containsExactly(uniqueLead);
  }

  @Test
  @DisplayName("Should reject duplicate when email already exists")
  void shouldRejectDuplicateWhenEmailAlreadyExists() {
    // Given
    LeadStorage storage = new LeadStorage();
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    Lead existingLead = new Lead(id1, contact("ivan@mail.ru", "+7123"), "TechCorp", "NEW");
    Lead duplicateLead = new Lead(id2, contact("ivan@mail.ru", "+7456"), "Other", "NEW");
    storage.add(existingLead);

    // When
    boolean added = storage.add(duplicateLead);

    // Then
    assertThat(added).isFalse();
    assertThat(storage.size()).isEqualTo(1);
    assertThat(storage.findAll()).containsExactly(existingLead);
  }

  @Test
  @DisplayName("Should throw exception when storage is full")
  void shouldThrowExceptionWhenStorageIsFull() {
    // Given: Заполни хранилище 100 лидами
    LeadStorage storage = new LeadStorage();
    for (int index = 0; index < 100; index++) {
      Address address = new Address("City", "Street", "000000");
      Contact contact = new Contact("lead" + index + "@mail.ru", "+7000", address);
      storage.add(new Lead(UUID.randomUUID(), contact, "Company", "NEW"));
    }

    // When + Then: 101-й лид должен выбросить исключение
    Contact hundredFirstContact = new Contact("lead101@mail.ru", "+7001",
        new Address("City", "Street", "000000"));
    Lead hundredFirstLead = new Lead(UUID.randomUUID(), hundredFirstContact, "Company", "NEW");

    assertThatThrownBy(() -> storage.add(hundredFirstLead))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Storage is full");
  }

  @Test
  @DisplayName("Should return only added leads when find all called")
  void shouldReturnOnlyAddedLeadsWhenFindAllCalled() {
    // Given
    LeadStorage storage = new LeadStorage();
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    Lead firstLead = new Lead(id1, contact("ivan@mail.ru", "+7123"), "TechCorp", "NEW");
    Lead secondLead = new Lead(id2, contact("maria@startup.io", "+7456"), "StartupLab", "NEW");
    storage.add(firstLead);
    storage.add(secondLead);

    // When
    Lead[] result = storage.findAll();

    // Then
    assertThat(result).hasSize(2);
    assertThat(result).containsExactly(firstLead, secondLead);
  }

  @Test
  @DisplayName("Should return lead when find by id with existing id")
  void shouldReturnLeadWhenFindByIdWithExistingId() {
    // Given
    LeadStorage storage = new LeadStorage();
    UUID id = UUID.randomUUID();
    Lead lead = new Lead(id, contact("ivan@mail.ru", "+7123"), "TechCorp", "NEW");
    storage.add(lead);

    // When
    Lead found = storage.findById(id);

    // Then
    assertThat(found).isEqualTo(lead);
  }

  @Test
  @DisplayName("Should return null for non existent id")
  void shouldReturnNullWhenFindByIdWithNonExistentId() {
    // Given
    LeadStorage storage = new LeadStorage();
    storage.add(new Lead(UUID.randomUUID(), contact("ivan@mail.ru", "+7123"), "TechCorp", "NEW"));

    // When
    Lead found = storage.findById(UUID.randomUUID());

    // Then
    assertThat(found).isNull();
  }
}
