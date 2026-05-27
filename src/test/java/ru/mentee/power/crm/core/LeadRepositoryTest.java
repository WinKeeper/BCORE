package ru.mentee.power.crm.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

class LeadRepositoryTest {

  @Test
  @DisplayName("Should automatically deduplicate leads by id")
  void shouldDeduplicateLeadsById() {
    // Given - создать LeadRepository и лид с UUID
    LeadRepository repository = new LeadRepository();
    Lead lead = new Lead(UUID.randomUUID(),
        new Contact("test@mail.ru", "+7000", new Address("Moscow", "Tverskaya", "123456")),
        "Company", "NEW");

    // When - добавить лид дважды через add
    repository.add(lead);
    boolean wasAdded = repository.add(lead);

    // Then - проверить что size == 1, второй add вернул false
    assertThat(repository.size()).isEqualTo(1);
    assertThat(wasAdded).isFalse();
  }

  @Test
  @DisplayName("Should allow different leads with different ids")
  void shouldAllowDifferentLeads() {
    // Given - создать два лида с разными
    LeadRepository repository = new LeadRepository();
    Lead firstLead = new Lead(UUID.randomUUID(),
        new Contact("test@mail.ru", "+7000", new Address("Moscow", "Tverskaya", "123456")),
        "Company", "NEW");
    Lead secondLead = new Lead(UUID.randomUUID(),
        new Contact("test@mail.ru", "+7000", new Address("Moscow", "Tverskaya", "123456")),
        "Company", "NEW");

    // When - добавить оба лида
    boolean wasFirstLeadAdded = repository.add(firstLead);
    boolean wasSecondLeadAdded = repository.add(secondLead);

    // Then - проверить что size == 2, оба add вернули true
    assertThat(repository.size()).isEqualTo(2);
    assertThat(wasFirstLeadAdded).isTrue();
    assertThat(wasSecondLeadAdded).isTrue();

  }

  @Test
  @DisplayName("Should find existing lead through contains")
  void shouldFindExistingLead() {
    // Given - добавить лид в репозиторий
    LeadRepository repository = new LeadRepository();
    Lead lead = new Lead(UUID.randomUUID(),
        new Contact("test@mail.ru", "+7000", new Address("Moscow", "Tverskaya", "123456")),
        "Company", "NEW");
    repository.add(lead);

    // When - вызвать contains с тем же лидом
    boolean hasLead = repository.contains(lead);

    // Then - проверить что contains вернул true
    assertThat(hasLead).isTrue();
  }

  @Test
  @DisplayName("Should return unmodifiable set from findAll")
  void shouldReturnUnmodifiableSet() {
    // Given - добавить лид в репозиторий
    LeadRepository repository = new LeadRepository();
    Lead lead = new Lead(UUID.randomUUID(),
        new Contact("test@mail.ru", "+7000", new Address("Moscow", "Tverskaya", "123456")),
        "Company", "NEW");
    repository.add(lead);

    // When - вызвать findAll и попытаться изменить результат
    Set<Lead> result = repository.findAll();

    // Then - проверить что выбрасывается UnsupportedOperationException
    assertThatThrownBy(() -> result.add(lead))
        .isInstanceOf(UnsupportedOperationException.class);

    assertThatThrownBy(() -> result.remove(lead))
        .isInstanceOf(UnsupportedOperationException.class);

    assertThatThrownBy(() -> result.clear())
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  @DisplayName("Should perform contains faster than array list")
  void shouldPerformFasterThanArrayList() {
    Address address = new Address("Moscow", "Tverskaya", "123456");
    Contact contact = new Contact("test@mail.ru", "+7000", address);

    Set<Lead> hashSet = new HashSet<>();
    List<Lead> arrayList = new ArrayList<>();

    for (int i = 0; i < 10000; i++) {
      Lead lead = new Lead(UUID.randomUUID(), contact, "Company", "NEW");
      hashSet.add(lead);
      arrayList.add(lead);
    }

    Lead target = new Lead(UUID.randomUUID(), contact, "Company", "NEW");
    hashSet.add(target);
    arrayList.add(target);

    long startHashSet = System.nanoTime();
    for (int i = 0; i < 1000; i++) {
      hashSet.contains(target);
    }
    long hashSetTime = System.nanoTime() - startHashSet;

    long startArrayList = System.nanoTime();
    for (int i = 0; i < 1000; i++) {
      arrayList.contains(target);
    }
    long arrayListTime = System.nanoTime() - startArrayList;

    int speedup = (int) (arrayListTime / hashSetTime);
    assertThat(speedup).as("HashSet should be 100x faster than ArrayList")
        .isGreaterThan(100);
  }
}