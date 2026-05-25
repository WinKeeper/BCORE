package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class LeadTest {

  @Test
  void shouldReturnIdWhenGetIdCallen() {
    // Given
    UUID uuid = UUID.randomUUID();
    Lead lead = new Lead(uuid, "alibaba@gmail.com", "12345", "alibaba", "NEW");

    // Then
    assertThat(lead.getId()).isEqualTo(uuid);
  }

  @Test
  void shouldReturnEmailWhenGetEmailCallen() {
    // Given
    Lead lead = new Lead(UUID.randomUUID(), "alibaba@gmail.com", "12345", "alibaba", "NEW");

    // When
    String email = lead.getEmail();

    // Then
    assertThat(email).isEqualTo("alibaba@gmail.com");
  }

  @Test
  void shouldReturnPhoneWhenGetPhoneCallen() {
    // Given
    Lead lead = new Lead(UUID.randomUUID(), "alibaba@gmail.com", "12345", "alibaba", "NEW");

    // When
    String phone = lead.getPhone();

    // Then
    assertThat(phone).isEqualTo("12345");
  }

  @Test
  void shouldReturnCompanyWhenGetCompanyCallen() {
    // Given
    Lead lead = new Lead(UUID.randomUUID(), "alibaba@gmail.com", "12345", "alibaba", "NEW");

    // When
    String company = lead.getCompany();

    // Then
    assertThat(company).isEqualTo("alibaba");
  }

  @Test
  void shouldReturnStatusWhenGetStatusCallen() {
    // Given
    Lead lead = new Lead(UUID.randomUUID(), "alibaba@gmail.com", "12345", "alibaba", "NEW");

    // When
    String status = lead.getStatus();

    // Then
    assertThat(status).isEqualTo("NEW");
  }

  @Test
  void shouldReturnFormattedStringWhenToStringCalled() {
    // Given
    UUID uuid = UUID.randomUUID();
    Lead lead = new Lead(uuid, "alibaba@gmail.com", "12345", "alibaba", "NEW");

    // Then
    String expected = ("Lead info : {id='%s', email='alibaba@gmail.com', phone='12345', " +
        "company='alibaba', status='NEW'}")
        .formatted(uuid);
    assertThat(lead.toString()).isEqualTo(expected);

  }
}