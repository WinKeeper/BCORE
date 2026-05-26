package ru.mentee.power.crm.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

class InMemoryLeadRepositoryTest {

  @Test
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
  void shouldNotAddDublicateEntityWhenAddCalled() {
    // Given
    InMemoryLeadRepository repository = new InMemoryLeadRepository();

    UUID firstId = UUID.randomUUID();

    Lead firstLead = new Lead(firstId, new Contact("email@mail.com", "12345", new Address("Moscow",
        "Oak", "628400")), "Ez", "NEW");
    Lead secondLead = new Lead(firstId, new Contact("email@mail.com", "12345", new Address("Moscow",
        "Oak", "628400")), "Ez", "NEW");

    repository.add(firstLead);

    // Then
    assertThatThrownBy(() -> repository.add(secondLead))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("already exists");
  }

  @Test
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
  void shouldReturnDefensiveCopyWhenFindAllCalled() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();
    Lead lead = new Lead(UUID.randomUUID(),
        new Contact("test@mail.ru", "+7000", new Address("Moscow", "Tverskaya", "123456")),
        "Company", "NEW");
    repository.add(lead);

    List<Lead> firstCall = repository.findAll();
    firstCall.clear();

    assertThat(repository.findAll()).hasSize(1);
    assertThat(repository.findAll().get(0)).isEqualTo(lead);
  }

}