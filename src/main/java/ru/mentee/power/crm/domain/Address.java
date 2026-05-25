package ru.mentee.power.crm.domain;

public record Address(String city, String street, String zip) {
  public Address(String city, String street, String zip) {
    if (city == null || city.isBlank()) {
      throw new IllegalArgumentException("City must not be null or blank");
    }
    if (street == null || street.isBlank()) {
      throw new IllegalArgumentException("Street must not be null or blank");
    }
    if (zip == null || zip.isBlank()) {
      throw new IllegalArgumentException("Zip must not be null or blank");
    }
    this.city = city;
    this.street = street;
    this.zip = zip;
  }
}
