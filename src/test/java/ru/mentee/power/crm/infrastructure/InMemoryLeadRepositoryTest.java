package ru.mentee.power.crm.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

class InMemoryLeadRepositoryTest {

  @Test
  @DisplayName("Should add unique entity when add called")
  void shouldAddUniqueEntityWhenAddCalled() {
    // Given
    InMemoryLeadRepository repository = new InMemoryLeadRepository();

    UUID firstId = UUID.randomUUID();
    UUID secondId = UUID.randomUUID();

    Lead firstLead = new Lead(firstId, new Contact("email@mail.com", "12345", new Address("Moscow",
        "Oak", "628400")), "Ez", "NEW");
    Lead secondLead = new Lead(secondId, new Contact("email@mail.com", "12345", new Address("Moscow",
        "Oak", "628400")), "Ez", "NEW");

    // When
    repository.add(firstLead);
    repository.add(secondLead);

    // Then
    assertThat(repository.findById(secondId)).contains(secondLead);
  }

  @Test
  @DisplayName("Should not add duplicate entity")
  void shouldNotAddDublicateEntityWhenAddCalled() {
    // Given
    InMemoryLeadRepository repository = new InMemoryLeadRepository();

    UUID firstId = UUID.randomUUID();

    Lead firstLead = new Lead(firstId, new Contact("email@mail.com", "12345", new Address("Moscow",
        "Oak", "628400")), "Ez", "NEW");
    Lead secondLead = new Lead(firstId, new Contact("email@mail.com", "12345", new Address("Moscow",
        "Oak", "628400")), "Ez", "NEW");

    repository.add(firstLead);
    repository.add(secondLead);  // дубликат — игнорируется

    // Then
    assertThat(repository.findAll()).hasSize(1);
  }

  @Test
  @DisplayName("Should find entity when find by id called")
  void shouldFindEntityWhenFindByIdCalled() {
    // Given
    InMemoryLeadRepository repository = new InMemoryLeadRepository();

    UUID firstId = UUID.randomUUID();
    UUID secondId = UUID.randomUUID();

    Lead firstLead = new Lead(firstId, new Contact("email@mail.com", "12345", new Address("Moscow",
        "Oak", "628400")), "Ez", "NEW");
    Lead secondLead = new Lead(secondId, new Contact("email@mail.com", "12345", new Address("Moscow",
        "Oak", "628400")), "Ez", "NEW");

    // When
    repository.add(firstLead);
    repository.add(secondLead);

    // Then
    assertThat(repository.findById(firstId)).contains(firstLead);
  }

  @Test
  @DisplayName("Should return empty optional for non existent id")
  void shouldNotFindEntityWhenFindByIdCalled() {
    // Given
    InMemoryLeadRepository repository = new InMemoryLeadRepository();

    UUID firstId = UUID.randomUUID();
    UUID secondId = UUID.randomUUID();

    Lead firstLead = new Lead(firstId, new Contact("email@mail.com", "12345", new Address("Moscow",
        "Oak", "628400")), "Ez", "NEW");

    repository.add(firstLead);

    // Then
    assertThat(repository.findById(secondId)).isEmpty();
  }

  @Test
  @DisplayName("Should remove existing entity when remove called")
  void shouldRemoveExistingEntityWhenRemoveCalled() {
    // Given
    InMemoryLeadRepository repository = new InMemoryLeadRepository();

    UUID firstId = UUID.randomUUID();

    Lead firstLead = new Lead(firstId, new Contact("email@mail.com", "12345", new Address("Moscow",
        "Oak", "628400")), "Ez", "NEW");

    repository.add(firstLead);

    // When
    repository.remove(firstId);

    // Then
    assertThat(repository.findById(firstId)).isEmpty();
  }

  @Test
  @DisplayName("Should return defensive copy when find all called")
  void shouldReturnDefensiveCopyWhenFindAllCalled() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();
    Lead lead = new Lead(UUID.randomUUID(),
        new Contact("test@mail.ru", "+7000", new Address("Moscow", "Tverskaya", "123456")),
        "Company", "NEW");
    repository.add(lead);

    List<Lead> firstCall = repository.findAll();
    firstCall.clear();

    assertThat(repository.findAll()).hasSize(1);
    assertThat(repository.findAll().getFirst()).isEqualTo(lead);
  }

}
