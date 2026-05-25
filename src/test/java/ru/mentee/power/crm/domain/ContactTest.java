package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ContactTest {

  @Test
  void shouldCreateContactWhenValidData() {
    // Given
    Contact contact = new Contact("Rabbit", "White", "white_rabbit@gmail.com");

    // Then
    assertThat(contact).extracting(Contact::getFirstName, Contact::getLastName,
        Contact::getEmail).containsExactly("Rabbit", "White", "white_rabbit@gmail.com");

  }

  @Test
  void shouldBeEqualWhenSameData() {
    // Given
    Contact firstContact = new Contact("Rabbit", "White", "white_rabbit@gmail.com");
    Contact secondContact = new Contact("Rabbit", "White", "white_rabbit@gmail.com");

    // Then
    assertThat(firstContact).isEqualTo(secondContact);
    assertThat(firstContact.hashCode()).isEqualTo(secondContact.hashCode());
  }

  @Test
  void shouldNotBeEqualWhenSameData() {
    // Given
    Contact firstContact = new Contact("Rabbit", "White", "white_rabbit@gmail.com");
    Contact secondContact = new Contact("Wolf", "White", "white_wolf@gmail.com");

    // Then
    assertThat(firstContact).isNotEqualTo(secondContact);
    assertThat(firstContact.hashCode()).isNotEqualTo(secondContact.hashCode());
  }
}