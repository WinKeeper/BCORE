package ru.mentee.power.crm.domain;

public record Address(String city, String street, String zip) {

  public Address(String city, String street, String zip) {
    if (city == null) {
      throw new IllegalArgumentException("City must not be null");
    }

    this.city = city;
    this.street = street;
    this.zip = zip;
  }

}
