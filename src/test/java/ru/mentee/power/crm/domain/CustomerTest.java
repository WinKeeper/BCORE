package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CustomerTest {
  @Test
  @DisplayName("Should reuse contact when creating customer")
  void shouldReuseContactWhenCreatingCustomer() {
    // Given
    UUID id1 = UUID.randomUUID();
    Address contactAddress = new Address("firstCity", "Street one", "628400");
    Address billingAddress = new Address("firstCity", "Street two", "628402");
    Contact contact = new Contact("email@mail.com", "12345", contactAddress);
    Customer customer = new Customer(id1, contact, billingAddress, "LOYAL");

    // Then
    assertThat(customer.contact()).isEqualTo(contact);
    assertThat(customer.contact().address()).isEqualTo(contactAddress);
    assertThat(customer.billingAddress()).isEqualTo(billingAddress);
    assertThat(customer.contact().address()).isNotSameAs(customer.billingAddress());
    assertThat(customer.loyaltyTier()).isEqualTo("LOYAL");
  }

  @Test
  @DisplayName("Should demonstrate contact reuse across lead and customer")
  void shouldDemonstrateContactReuseAcrossLeadAndCustomer() {
    // Given: один Contact
    Address address = new Address("Moscow", "Tverskaya", "123456");
    Contact contact = new Contact("shared@mail.ru", "+7000", address);

    // When: используем один Contact и в Lead, и в Customer
    Lead lead = new Lead(UUID.randomUUID(), contact, "Company", "NEW");
    Customer customer = new Customer(UUID.randomUUID(), contact, address, "LOYAL");

    // Then: один и тот же объект Contact переиспользован
    assertThat(lead.contact()).isSameAs(customer.contact());  // одна ссылка
    assertThat(lead.contact().email()).isEqualTo("shared@mail.ru");
    assertThat(customer.contact().address().city()).isEqualTo("Moscow");
  }
}
