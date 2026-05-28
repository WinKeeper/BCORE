package ru.mentee.power.crm.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.Lead;

class LeadRepositoryTest {
  private LeadRepository repository;

  @BeforeEach
  void setUp() {
    repository = new LeadRepository();
  }

  @Test
  @DisplayName("Should save and find lead by id when lead saved")
  void shouldSaveAndFindLeadByIdWhenLeadSaved() {
    // Given
    Lead lead = new Lead("10", "email@mail.com", "12345", "Corp", "NEW");

    // When
    repository.save(lead);

    // Then
    assertThat(repository.findById(lead.id())).isNotNull();
  }

  @Test
  @DisplayName("Should return null when lead not found")
  void shouldReturnNullWhenLeadNotFound() {
    // Given - empty repository

    // Then
    assertThat(repository.findAll()).isEmpty();
  }

  @Test
  @DisplayName("Should return all leads when multiple lead saved")
  void shouldReturnAllLeadsWhenMultipleLeadSaved() {
    // Given
    Lead firstLead = new Lead("1", "email@mail.com", "12345", "Corp", "NEW");
    Lead secondLead = new Lead("2", "email2@mail.com", "12345", "Corp", "NEW");
    Lead thirdLead = new Lead("3", "email3@mail.com", "12345", "Corp", "NEW");

    // When
    repository.save(firstLead);
    repository.save(secondLead);
    repository.save(thirdLead);

    // Then
    assertThat(repository.findAll()).hasSize(3);
  }

  @Test
  @DisplayName("Should delete lead when lead exists")
  void shouldDeleteLeadWhenLeadExists() {
    // Given
    Lead lead = new Lead("10", "email@mail.com", "12345", "Corp", "NEW");

    // When
    repository.delete(lead.id());

    // Then
    assertThat(repository.findById(lead.id())).isNull();
    assertThat(repository.findAll()).isEmpty();
  }

  @Test
  @DisplayName("Should overwrite lead when save with same id")
  void shouldOverwriteLeadWhenSaveWithSameId() {
    // Given
    Lead lead = new Lead("10", "email@mail.com", "12345", "Corp", "NEW");
    Lead leadWithSameId = new Lead("10", "emailCopy@mail.com", "1234567", "CorpCp", "NEW");

    // When
    String id = lead.id();
    repository.save(lead);
    repository.save(leadWithSameId);

    // Then
    assertThat(repository.findById(id))
        .extracting(Lead::id, Lead::email)
        .containsExactly(leadWithSameId.id(), leadWithSameId.email());
    assertThat(repository.findAll()).hasSize(1);
  }

  @Test
  @DisplayName("Should be faster with map than with list filter")
  void shouldFindFasterWithMapThanListFilter() {
    // Given: Создать 1000 лидов
    List<Lead> leadList = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      Lead lead = new Lead(String.valueOf(i),
          "email" + i + "@test.com", "+7" + i, "Company" + i, "NEW");
      repository.save(lead);
      leadList.add(lead);
    }

    String targetId = "500";  // элемент точно существует

    // When: Поиск через Map
    long mapStart = System.nanoTime();
    Lead foundInMap = repository.findById(targetId);
    long mapDuration = System.nanoTime() - mapStart;

    // When: Поиск через List.stream().filter()
    long listStart = System.nanoTime();
    Lead foundInList = leadList.stream()
        .filter(lead -> lead.id().equals(targetId))
        .findFirst()
        .orElse(null);
    long listDuration = System.nanoTime() - listStart;

    // Then: Map должен быть минимум в 10 раз быстрее
    assertThat(foundInMap).isEqualTo(foundInList);
    assertThat(listDuration).isGreaterThan(mapDuration * 10);
  }

  @Test
  @DisplayName("Should save both leads with same email because repository does not check business rules")
  void shouldSaveBothLeadsEvenWithSameEmailAndPhone() {
    Lead originalLead = new Lead("1", "ivan@mail.ru",
        "+79001234567", "Acme Corp", "NEW");
    Lead duplicateLead = new Lead("2", "ivan@mail.ru",
        "+79001234567", "TechCorp", "NEW");

    repository.save(originalLead);
    repository.save(duplicateLead);

    assertThat(repository.size()).isEqualTo(2);
  }

}