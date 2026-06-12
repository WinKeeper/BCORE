package ru.mentee.power.crm.spring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.InMemoryLeadRepository;
import ru.mentee.power.crm.repository.LeadRepository;

class LeadServiceTest {

  private LeadService service;
  private LeadRepository<Lead> repository;

  @BeforeEach
  void setUp() {
    repository = new InMemoryLeadRepository();
    service = new LeadService(repository);
  }

  @Test
  @DisplayName("Should create lead when email is unique")
  void shouldCreateLeadWhenEmailIsUnique() {
    // Given
    String email = "test@example.com";
    String company = "Test Company";
    LeadStatus status = LeadStatus.NEW;

    // When
    Lead result = service.addLead(email, "+71234567890", company, status);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.email()).isEqualTo(email);
    assertThat(result.company()).isEqualTo(company);
    assertThat(result.status()).isEqualTo(status);
    assertThat(result.id()).isNotNull();
  }

  @Test
  @DisplayName("Should throw exception when email already exists")
  void shouldThrowExceptionWhenEmailAlreadyExists() {
    // Given
    String email = "duplicate@example.com";
    service.addLead(email, "+71234567890", "First Company", LeadStatus.NEW);

    // When/Then
    assertThatThrownBy(() ->
        service.addLead(email, "+71234567890", "Second Company", LeadStatus.NEW)
    )
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Lead with email already exists: " + email);
  }

  @Test
  @DisplayName("Should find all leads")
  void shouldFindAllLeads() {
    // Given
    service.addLead("one@example.com", "+71234567890", "Company 1", LeadStatus.NEW);
    service.addLead("two@example.com", "+79876543210", "Company 2", LeadStatus.CONTACTED);

    // When
    List<Lead> result = service.findAll();

    // Then
    assertThat(result).hasSize(2);
  }

  @Test
  @DisplayName("Should find lead by id")
  void shouldFindLeadById() {
    // Given
    Lead created = service.addLead("find@example.com", "+71234567890", "Company", LeadStatus.NEW);

    // When
    Optional<Lead> result = service.findById(created.id());

    // Then
    assertThat(result).isPresent();
    assertThat(result.get().email()).isEqualTo("find@example.com");
  }

  @Test
  @DisplayName("Should find lead by email")
  void shouldFindLeadByEmail() {
    // Given
    service.addLead("search@example.com", "+71234567890", "Company", LeadStatus.NEW);

    // When
    Optional<Lead> result = service.findByEmail("search@example.com");

    // Then
    assertThat(result).isPresent();
    assertThat(result.get().company()).isEqualTo("Company");
  }

  @Test
  @DisplayName("Should return empty when lead not found")
  void shouldReturnEmptyWhenLeadNotFound() {
    // Given/When
    Optional<Lead> result = service.findByEmail("nonexistent@example.com");

    // Then
    assertThat(result).isEmpty();
  }

  private void seedStandardLeads() {
    for (int i = 0; i < 3; i++) {
      service.addLead("new" + i + "@mail.ru", "+7900" + i, "Corp #" + i, LeadStatus.NEW);
    }
    for (int i = 0; i < 5; i++) {
      service.addLead("contacted" + i + "@mail.ru", "+7900" + i, "Corp #" + i, LeadStatus.CONTACTED);
    }
    for (int i = 0; i < 2; i++) {
      service.addLead("qualified" + i + "@mail.ru", "+7900" + i, "Corp #" + i, LeadStatus.QUALIFIED);
    }
  }

  @ParameterizedTest
  @DisplayName("Should filter leads by status")
  @CsvSource({
      "NEW, 3",
      "CONTACTED, 5",
      "QUALIFIED, 2"
  })
  void shouldFilterLeadsByStatus(LeadStatus status, int expectedCount) {
    seedStandardLeads();

    List<Lead> result = service.findByStatus(status);

    assertThat(result).hasSize(expectedCount);
    assertThat(result).allMatch(lead -> lead.status().equals(status));
  }

  @Test
  @DisplayName("Should return empty list when no leads")
  void shouldReturnEmptyListWhenNoLeads() {
    assertThat(service.findByStatus(LeadStatus.NEW)).isEmpty();
  }

  @Test
  @DisplayName("Should return empty list when no leads with status CONVERTED")
  void shouldReturnEmptyListWhenNoLeadsWithStatusConverted() {
    seedStandardLeads();

    List<Lead> result = service.findByStatus(LeadStatus.CONVERTED);

    assertThat(result).hasSize(0);
  }

}