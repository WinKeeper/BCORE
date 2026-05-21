package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LeadTest {

  @Test
  void shouldReturnIdWhenGetIdCallen() {
    // Given
    Lead lead = new Lead("5", "alibaba@gmail.com", "12345", "alibaba", "NEW");

    // When
    String id = lead.getId();

    // Then
    assertThat(id).isEqualTo("5");
  }

  @Test
  void shouldReturnEmailWhenGetEmailCallen() {
    // Given
    Lead lead = new Lead("5", "alibaba@gmail.com", "12345", "alibaba", "NEW");

    // When
    String email = lead.getEmail();

    // Then
    assertThat(email).isEqualTo("alibaba@gmail.com");
  }

  @Test
  void shouldReturnPhoneWhenGetPhoneCallen() {
    // Given
    Lead lead = new Lead("5", "alibaba@gmail.com", "12345", "alibaba", "NEW");

    // When
    String phone = lead.getPhone();

    // Then
    assertThat(phone).isEqualTo("12345");
  }

  @Test
  void shouldReturnCompanyWhenGetCompanyCallen() {
    // Given
    Lead lead = new Lead("5", "alibaba@gmail.com", "12345", "alibaba", "NEW");

    // When
    String company = lead.getCompany();

    // Then
    assertThat(company).isEqualTo("alibaba");
  }

  @Test
  void shouldReturnStatusWhenGetStatusCallen() {
    // Given
    Lead lead = new Lead("5", "alibaba@gmail.com", "12345", "alibaba", "NEW");

    // When
    String status = lead.getStatus();

    // Then
    assertThat(status).isEqualTo("NEW");
  }

  @Test
  void shouldReturnFormattedStringWhenToStringCalled() {
    // Given
    Lead lead = new Lead("5", "alibaba@gmail.com", "12345", "alibaba", "NEW");

    // When
    String leadInfo = lead.toString();

    // Then
    assertThat(leadInfo).isEqualTo("Lead info : {id='5', email='alibaba@gmail.com', phone='12345', "
        + "company='alibaba', status='NEW'}");
  }
}