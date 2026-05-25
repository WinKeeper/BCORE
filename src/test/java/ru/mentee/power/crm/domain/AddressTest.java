package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;

class AddressTest {

  @Test
  void shouldCreateAddressWhenValidData() {
    Address address = new Address("Москва", "Декабристов", "624000");

    assertThat(address).extracting(Address::city, Address::street, Address::zip).containsExactly(
        "Москва", "Декабристов", "624000");
  }

  @Test
  void shouldBeEqualWhenSameData() {
    Address firstAddress = new Address("Москва", "Декабристов", "624000");
    Address secondAddress = new Address("Москва", "Декабристов", "624000");

    assertThat(firstAddress).isEqualTo(secondAddress);
    assertThat(firstAddress.hashCode()).isEqualTo(secondAddress.hashCode());
  }

  @Test
  void shouldThrowExceptionWhenCityIsNull() {
    assertThatIllegalArgumentException().isThrownBy(() -> new Address(null, "Декабристов",
        "624000")).withMessage("City must not be null or blank");
  }

  @Test
  void shouldThrowExceptionWhenZipIsBlank() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Address("Москва", "Декабристов", ""))
        .withMessage("Zip must not be null or blank");
  }
}