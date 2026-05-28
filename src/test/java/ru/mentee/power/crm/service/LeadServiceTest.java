package ru.mentee.power.crm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
  void shouldReturnEmptyWhenLeadNotFound() {
    // Given/When
    Optional<Lead> result = service.findByEmail("nonexistent@example.com");

    // Then
    assertThat(result).isEmpty();
  }
}