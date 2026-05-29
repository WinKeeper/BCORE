# Сравнение: new внутри vs DI через конструктор

## BAD: new InMemoryLeadRepository() внутри класса

```java
public class LeadService {
  // Тесная связанность!
  private final LeadRepository repository = new InMemoryLeadRepository();
}
```

## GOOD: DI через конструктор

```java
public class LeadService {
  private final LeadRepository repository;

  // Передаём ссылку через конструктор
  public LeadService(LeadRepository repository) {
    this.repository = repository;
  }
}
```

Преимущества:

    В тестах передаём mock(LeadRepository.class)
    В production передаём InMemoryLeadRepository
    В будущем передаём JpaLeadRepository (Sprint 7)
    Зависимость явная — видно в конструкторе
