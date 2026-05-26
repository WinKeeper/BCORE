package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ContactTest {

  @Test
  void shouldCreateContactWhenValidData() {
    Address address = new Address("Москва", "Декабристов", "628400");
    Contact contact = new Contact("rabbit@mail.ru", "+71234567890", address);

    assertThat(contact.email()).isEqualTo("rabbit@mail.ru");
    assertThat(contact.phone()).isEqualTo("+71234567890");
    assertThat(contact.address()).isEqualTo(address);
    assertThat(contact.address().city()).isEqualTo("Москва");
  }

  @Test
  void shouldBeEqualWhenSameData() {
    // Given
    Address address = new Address("Москва", "Декабристов", "628400");
    Contact firstContact = new Contact("rabbit@mail.ru", "+71234567890", address);
    Contact secondContact = new Contact("rabbit@mail.ru", "+71234567890", address);

    // Then
    assertThat(firstContact).isEqualTo(secondContact);
    assertThat(firstContact.hashCode()).isEqualTo(secondContact.hashCode());
  }

  @Test
  void shouldNotBeEqualWhenSameData() {
    // Given
    Address address = new Address("Москва", "Декабристов", "628400");
    Contact firstContact = new Contact("rabbit@mail.ru", "+71234567890", address);
    Contact secondContact = new Contact("wolf@mail.ru", "+71234567890", address);

    // Then
    assertThat(firstContact).isNotEqualTo(secondContact);
    assertThat(firstContact.hashCode()).isNotEqualTo(secondContact.hashCode());
  }

  @Test
  void shouldDelegateToAddressWhenAccessingCity() {
    // Given
    Address address = new Address("Москва", "Декабристов", "628400");
    Contact contact = new Contact("rabbit@mail.ru", "+71234567890", address);

    // Then
    assertThat(contact.address().city()).isEqualTo("Москва");
    assertThat(contact.address().street()).isEqualTo("Декабристов");
    assertThat(contact.address().zip()).isEqualTo("628400");
  }

  @Test
  void shouldThrowExceptionWhenAddressIsNull() {
    assertThatThrownBy(() -> new Contact("rabbit@mail.ru", "+71234567890", null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Address");
  }
}
