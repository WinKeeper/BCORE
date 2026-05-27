package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LeadEqualsHashCodeTest {

  private static Contact createContact() {
    return new Contact("ivan@mail.ru", "+7123", new Address("Moscow", "Tverskaya", "123456"));
  }

  @Test
  @DisplayName("Should be reflexive when equals called on same object")
  void shouldBeReflexiveWhenEqualsCalledOnSameObject() {
    // Given
    Lead lead = new Lead(UUID.randomUUID(), createContact(), "TechCorp", "NEW");

    // Then: Объект равен сам себе (isEqualTo использует equals() внутри)
    assertThat(lead).isEqualTo(lead);
  }

  @Test
  @DisplayName("Should be symmetric when equals called on two objects")
  void shouldBeSymmetricWhenEqualsCalledOnTwoObjects() {
    // Given
    UUID id = UUID.randomUUID();
    Lead firstLead = new Lead(id, createContact(), "TechCorp", "NEW");
    Lead secondLead = new Lead(id, createContact(), "TechCorp", "NEW");

    // Then: Симметричность — порядок сравнения не важен
    assertThat(firstLead).isEqualTo(secondLead);
    assertThat(secondLead).isEqualTo(firstLead);
  }

  @Test
  @DisplayName("Should be transitive for chain of three objects")
  void shouldBeTransitiveWhenEqualsChainOfThreeObjects() {
    // Given
    UUID id = UUID.randomUUID();
    Lead firstLead = new Lead(id, createContact(), "TechCorp", "NEW");
    Lead secondLead = new Lead(id, createContact(), "TechCorp", "NEW");
    Lead thirdLead = new Lead(id, createContact(), "TechCorp", "NEW");

    // Then: Транзитивность — если A=B и B=C, то A=C
    assertThat(firstLead).isEqualTo(secondLead);
    assertThat(secondLead).isEqualTo(thirdLead);
    assertThat(firstLead).isEqualTo(thirdLead);
  }

  @Test
  @DisplayName("Should be consistent when equals called multiple times")
  void shouldBeConsistentWhenEqualsCalledMultipleTimes() {
    // Given
    UUID id = UUID.randomUUID();
    Lead firstLead = new Lead(id, createContact(), "TechCorp", "NEW");
    Lead secondLead = new Lead(id, createContact(), "TechCorp", "NEW");

    // Then: Результат одинаковый при многократных вызовах
    assertThat(firstLead).isEqualTo(secondLead);
    assertThat(firstLead).isEqualTo(secondLead);
    assertThat(firstLead).isEqualTo(secondLead);
  }

  @Test
  @DisplayName("Should return false when equals compared with null")
  void shouldReturnFalseWhenEqualsComparedWithNull() {
    // Given
    Lead lead = new Lead(UUID.randomUUID(), createContact(), "TechCorp", "NEW");

    // Then: Объект не равен null (isNotEqualTo проверяет equals(null) = false)
    assertThat(lead).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should have same hash code when objects are equal")
  void shouldHaveSameHashCodeWhenObjectsAreEqual() {
    // Given
    UUID id = UUID.randomUUID();
    Lead firstLead = new Lead(id, createContact(), "TechCorp", "NEW");
    Lead secondLead = new Lead(id, createContact(), "TechCorp", "NEW");

    // Then: Если объекты равны, то hashCode должен быть одинаковым
    assertThat(firstLead).isEqualTo(secondLead);
    assertThat(firstLead.hashCode()).isEqualTo(secondLead.hashCode());
  }

  @Test
  @DisplayName("Should work in hash map when lead used as key")
  void shouldWorkInHashMapWhenLeadUsedAsKey() {
    // Given
    UUID id = UUID.randomUUID();
    Lead keyLead = new Lead(id, createContact(), "TechCorp", "NEW");
    Lead lookupLead = new Lead(id, createContact(), "TechCorp", "NEW");

    Map<Lead, String> map = new HashMap<>();
    map.put(keyLead, "CONTACTED");

    // When: Получаем значение по другому объекту с тем же id
    String status = map.get(lookupLead);

    // Then: HashMap нашел значение благодаря equals/hashCode
    assertThat(status).isEqualTo("CONTACTED");
  }

  @Test
  @DisplayName("Should not be equal when ids are different")
  void shouldNotBeEqualWhenIdsAreDifferent() {
    // Given
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    Lead firstLead = new Lead(id1, createContact(), "TechCorp", "NEW");
    Lead differentLead = new Lead(id2, createContact(), "TechCorp", "NEW");

    // Then: Разные id = разные объекты (isNotEqualTo использует equals() внутри)
    assertThat(firstLead).isNotEqualTo(differentLead);
  }
}
