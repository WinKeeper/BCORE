package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LeadTest {

  @Test
  @DisplayName("Should return id when get id called")
  void shouldReturnIdWhenGetIdCallen() {
    // Given
    UUID uuid = UUID.randomUUID();
    Address address = new Address("Moscow", "Tverskaya", "123456");
    Contact contact = new Contact("alibaba@gmail.com", "12345", address);
    Lead lead = new Lead(uuid, contact, "alibaba", "NEW");

    // Then
    assertThat(lead.id()).isEqualTo(uuid);
  }

  @Test
  @DisplayName("Should return email via contact delegation")
  void shouldReturnEmailWhenGetEmailCallen() {
    // Given
    Address address = new Address("Moscow", "Tverskaya", "123456");
    Contact contact = new Contact("alibaba@gmail.com", "12345", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "alibaba", "NEW");

    // When
    String email = lead.contact().email();

    // Then
    assertThat(email).isEqualTo("alibaba@gmail.com");
  }

  @Test
  @DisplayName("Should return phone via contact delegation")
  void shouldReturnPhoneWhenGetPhoneCallen() {
    // Given
    Address address = new Address("Moscow", "Tverskaya", "123456");
    Contact contact = new Contact("alibaba@gmail.com", "12345", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "alibaba", "NEW");

    // When
    String phone = lead.contact().phone();

    // Then
    assertThat(phone).isEqualTo("12345");
  }

  @Test
  @DisplayName("Should return company")
  void shouldReturnCompanyWhenGetCompanyCallen() {
    // Given
    Address address = new Address("Moscow", "Tverskaya", "123456");
    Contact contact = new Contact("alibaba@gmail.com", "12345", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "alibaba", "NEW");

    // When
    String company = lead.company();

    // Then
    assertThat(company).isEqualTo("alibaba");
  }

  @Test
  @DisplayName("Should return status")
  void shouldReturnStatusWhenGetStatusCallen() {
    // Given
    Address address = new Address("Moscow", "Tverskaya", "123456");
    Contact contact = new Contact("alibaba@gmail.com", "12345", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "alibaba", "NEW");

    // When
    String status = lead.status();

    // Then
    assertThat(status).isEqualTo("NEW");
  }

  @Test
  @DisplayName("Should return formatted string when to string called")
  void shouldReturnFormattedStringWhenToStringCalled() {
    // Given
    UUID uuid = UUID.randomUUID();
    Address address = new Address("Moscow", "Tverskaya", "123456");
    Contact contact = new Contact("alibaba@gmail.com", "12345", address);
    Lead lead = new Lead(uuid, contact, "alibaba", "NEW");

    // Then
    String expected = "Lead[id=" + uuid + ", contact=" + contact + ", company=alibaba, status=NEW]";
    assertThat(lead.toString()).isEqualTo(expected);
  }

  @Test
  @DisplayName("Should create lead when valid data")
  void shouldCreateLeadWhenValidData() {
    Address address = new Address("Moscow", "Tverskaya", "123456");
    Contact contact = new Contact("test@mail.ru", "+7000", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "Company", "NEW");

    assertThat(lead.contact()).isEqualTo(contact);
  }

  @Test
  @DisplayName("Should access email through delegation")
  void shouldAccessEmailThroughDelegationWhenLeadCreated() {
    Address address = new Address("Moscow", "Tverskaya", "123456");
    Contact contact = new Contact("test@mail.ru", "+7000", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "Company", "NEW");

    assertThat(lead.contact().email()).isEqualTo("test@mail.ru");
    assertThat(lead.contact().address().city()).isEqualTo("Moscow");
  }

  @Test
  @DisplayName("Should be equal when same id but different contact")
  void shouldBeEqualWhenSameIdButDifferentContact() {
    UUID id = UUID.randomUUID();
    Address address1 = new Address("Moscow", "Tverskaya", "123456");
    Address address2 = new Address("SPb", "Nevsky", "654321");
    Contact contact1 = new Contact("a@mail.ru", "+7000", address1);
    Contact contact2 = new Contact("b@mail.ru", "+7111", address2);
    Lead lead1 = new Lead(id, contact1, "Company", "NEW");
    Lead lead2 = new Lead(id, contact2, "Company", "NEW");

    assertThat(lead1).isEqualTo(lead2);
    assertThat(lead1.hashCode()).isEqualTo(lead2.hashCode());
  }

  @Test
  @DisplayName("Should throw exception when contact is null")
  void shouldThrowExceptionWhenContactIsNull() {
    assertThatThrownBy(() -> new Lead(UUID.randomUUID(), null, "Company", "NEW"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Contact");
  }

  @Test
  @DisplayName("Should throw exception when invalid status")
  void shouldThrowExceptionWhenInvalidStatus() {
    assertThatThrownBy(() -> new Lead(UUID.randomUUID(),
        new Contact("test@mail.ru", "+7000", new Address("Moscow", "Tverskaya", "123456")),
        "Company", "INVALID"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid status");
  }

  @Test
  @DisplayName("Should demonstrate three level composition")
  void shouldDemonstrateThreeLevelCompositionWhenAccessingCity() {
    Address address = new Address("Moscow", "Tverskaya", "123456");
    Contact contact = new Contact("test@mail.ru", "+7000", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "Company", "NEW");

    Contact leadContact = lead.contact();
    Address leadAddress = leadContact.address();
    String city = leadAddress.city();

    String cityInline = lead.contact().address().city();

    assertThat(city).isEqualTo("Moscow");
    assertThat(cityInline).isEqualTo(city);
  }
}
