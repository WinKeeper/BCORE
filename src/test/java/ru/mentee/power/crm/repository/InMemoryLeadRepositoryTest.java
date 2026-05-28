package ru.mentee.power.crm.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;

class InMemoryLeadRepositoryTest {

  @Test
  @DisplayName("Should save and find lead by id")
  void shouldSaveAndFindLead() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();

    UUID firstId = UUID.randomUUID();
    UUID secondId = UUID.randomUUID();

    Lead firstLead = new Lead(firstId, "email@mail.com", "12345", "Corp", LeadStatus.NEW);
    Lead secondLead = new Lead(secondId, "email2@mail.com", "54321", "Inc", LeadStatus.NEW);

    repository.save(firstLead);
    repository.save(secondLead);

    assertThat(repository.findById(secondId)).contains(secondLead);
    assertThat(repository.findById(firstId)).contains(firstLead);
  }

  @Test
  @DisplayName("Should overwrite existing lead when saved with same id")
  void shouldOverwriteLeadWithSameId() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();

    UUID id = UUID.randomUUID();

    Lead firstLead = new Lead(id, "old@mail.com", "1111", "OldCo", LeadStatus.NEW);
    Lead secondLead = new Lead(id, "new@mail.com", "2222", "NewCo", LeadStatus.CONTACTED);

    repository.save(firstLead);
    repository.save(secondLead);

    assertThat(repository.findAll()).hasSize(1);
    assertThat(repository.findById(id)).contains(secondLead);
  }

  @Test
  @DisplayName("Should find entity when find by id called")
  void shouldFindEntityWhenFindByIdCalled() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();

    UUID firstId = UUID.randomUUID();
    UUID secondId = UUID.randomUUID();

    Lead firstLead = new Lead(firstId, "email@mail.com", "12345", "Corp", LeadStatus.NEW);
    Lead secondLead = new Lead(secondId, "email2@mail.com", "54321", "Inc", LeadStatus.NEW);

    repository.save(firstLead);
    repository.save(secondLead);

    assertThat(repository.findById(firstId)).contains(firstLead);
  }

  @Test
  @DisplayName("Should return empty optional for non existent id")
  void shouldNotFindEntityWhenFindByIdCalled() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();

    UUID firstId = UUID.randomUUID();
    UUID secondId = UUID.randomUUID();

    Lead firstLead = new Lead(firstId, "email@mail.com", "12345", "Corp", LeadStatus.NEW);

    repository.save(firstLead);

    assertThat(repository.findById(secondId)).isEmpty();
  }

  @Test
  @DisplayName("Should delete existing entity by id")
  void shouldDeleteExistingEntityWhenDeleteCalled() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();

    UUID id = UUID.randomUUID();

    Lead lead = new Lead(id, "email@mail.com", "12345", "Corp", LeadStatus.NEW);

    repository.save(lead);
    repository.delete(id);

    assertThat(repository.findById(id)).isEmpty();
  }

  @Test
  @DisplayName("Should return defensive copy when find all called")
  void shouldReturnDefensiveCopyWhenFindAllCalled() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();
    Lead lead = new Lead(UUID.randomUUID(), "test@mail.com", "+7000", "Company", LeadStatus.NEW);
    repository.save(lead);

    List<Lead> firstCall = repository.findAll();
    firstCall.clear();

    assertThat(repository.findAll()).hasSize(1);
    assertThat(repository.findAll().getFirst()).isEqualTo(lead);
  }

  @Test
  @DisplayName("Should find lead by email when email exists")
  void shouldFindLeadByEmailWhenExists() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();

    Lead lead = new Lead(UUID.randomUUID(), "ivan@mail.ru", "+7123", "Corp", LeadStatus.NEW);
    repository.save(lead);

    assertThat(repository.findByEmail("ivan@mail.ru")).contains(lead);
  }

  @Test
  @DisplayName("Should return empty when email not found")
  void shouldReturnEmptyWhenEmailNotFound() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();

    assertThat(repository.findByEmail("unknown@mail.ru")).isEmpty();
  }

}
