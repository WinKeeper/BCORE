# BCORE

[![Style and test](https://github.com/WinKeeper/BCORE/actions/workflows/ci.yml/badge.svg?branch=master)](https://github.com/WinKeeper/BCORE/actions/workflows/ci.yml)

## Технологический стек проекта

### Языки и платформы

- **Java 25 LTS** — основной язык разработки
- **Gradle 9.x** — система сборки (через Gradle Wrapper)

### Инструменты качества кода

- **Checkstyle** — статический анализ стиля кода
    - Конфигурация: `config/checkstyle/checkstyle.xml`
    - Запуск: `./gradlew checkstyleMain`
- **JUnit 5** — фреймворк тестирования
    - Запуск: `./gradlew test`

### CI/CD

- **GitHub Actions** — автоматическая проверка PR
    - Checkstyle на каждый коммит
    - Тесты на каждый коммит
    - Конфигурация: `.github/workflows/`

### Правила кода

- Стиль: Google Java Style Guide (через Checkstyle)
- Коммиты: Conventional Commits (`feat:`, `fix:`, `docs:`)
- Ветки: `feature/DVT-X` для задач, `master` — основная
- Pull Request: обязателен для слияния в master

## BCORE-1: Первый класс Lead — От переменных к объектам

### Checklist

- [X] Gradle проект создан через IntelliJ IDEA (File → New → Project)
- [X] build.gradle обновлен (JUnit 5.11, AssertJ 3.26.3)
- [X] Lead.java создан в ru.mentee.power.crm.domain
- [X] Все 5 полей объявлены как private
- [X] Конструктор принимает 5 параметров
- [X] Getters для всех полей реализованы
- [X] toString() переопределен с @Override
- [X] LeadTest создан с минимумом 5 тестами
- [X] Тесты проходят (Ctrl+Ctrl → gradle test)
- [X] Coverage ≥80% (JaCoCo отчет проверен)

### Результат само-ревью BCORE-1

Ошибок не обнаружено согласно Code Review Checklist раздела.

## BCORE-2: Массив Lead[] и дедупликация — От одного объекта к коллекции

### Инсайты BCORE-2

- массив с простыми типами данных инициализируется с '0';
- массив объектов инициализируется с 'null';
- надо привыкать использовать for each;
- при вызове метода ```methodName()``` эквивалентно ```this.methodName()``` со ссылкой на текущий объект класса;
- AI хорошо помогает в плане объяснения в контексте самого проекта.

### Результат само-ревью BCORE-2

Ошибок не обнаружено согласно Code Review Checklist раздела.

## BCORE-3: Контракт equals/hashCode — Правильное сравнение объектов

### Контракт equals(): 5 обязательных свойств

Метод `equals()` должен соблюдать **контракт** — набор правил, которые гарантируют корректную работу в Java коллекциях.
Эти правила описаны в JavaDoc класса Object и являются частью спецификации Java.

**Свойство 1: Рефлексивность (Reflexive)** Любой объект равен сам себе: `x.equals(x)` всегда должен возвращать `true`.

```java
Lead lead = new Lead("1", "ivan@mail.ru", "+7123", "TechCorp", "NEW");
lead.

equals(lead);  // Должно быть true
```

Нарушение рефлексивности ломает коллекции: HashSet может не найти объект, который только что добавили.

**Свойство 2: Симметричность (Symmetric)** Если `x.equals(y)` возвращает `true`, то `y.equals(x)` тоже должен возвращать
`true`. Порядок сравнения не важен.

```java
Lead lead1 = new Lead("1", "ivan@mail.ru", "+7123", "TechCorp", "NEW");
Lead lead2 = new Lead("1", "ivan@mail.ru", "+7123", "TechCorp", "NEW");

if(lead1.

equals(lead2)){
    assert lead2.

equals(lead1);  // Должно быть true
}
```

**Свойство 3: Транзитивность (Transitive)** Если `x.equals(y)` и `y.equals(z)`, то `x.equals(z)` тоже должен быть
`true`. Цепочка равенства сохраняется.

```java
Lead lead1 = new Lead("1", "ivan@mail.ru", "+7123", "TechCorp", "NEW");
Lead lead2 = new Lead("1", "ivan@mail.ru", "+7123", "TechCorp", "NEW");
Lead lead3 = new Lead("1", "ivan@mail.ru", "+7123", "TechCorp", "NEW");

if(lead1.

equals(lead2) &&lead2.

equals(lead3)){
    assert lead1.

equals(lead3);  // Должно быть true
}
```

Нарушение транзитивности ведет к нелогичным результатам: объект равен A и B, но A не равен B.

**Свойство 4: Консистентность (Consistent)** Несколько вызовов `x.equals(y)` должны возвращать одинаковый результат,
если данные объектов не изменились.

```java
Lead lead1 = new Lead("1", "ivan@mail.ru", "+7123", "TechCorp", "NEW");
Lead lead2 = new Lead("1", "ivan@mail.ru", "+7123", "TechCorp", "NEW");

boolean result1 = lead1.equals(lead2);
boolean result2 = lead1.equals(lead2);
assert result1 ==result2;  // Должен быть одинаковым
```

Нарушение консистентности: equals() зависит от текущего времени или случайного значения — это ошибка.

**Свойство 5: null-безопасность (Non-nullity)** Любой объект не равен `null`: `x.equals(null)` всегда должен возвращать
`false`, никогда не выбрасывать NullPointerException.

```java
Lead lead = new Lead("1", "ivan@mail.ru", "+7123", "TechCorp", "NEW");
lead.

equals(null);  // Должно быть false, не NPE!
```

Если equals() не проверяет null, вызов `lead.equals(null)` выбросит NPE при попытке привести null к типу Lead — это
нарушение контракта.

**Главное правило:** Если переопределяешь `equals()`, **обязательно** переопределяй `hashCode()` тоже. Иначе объекты не
будут корректно работать в HashMap, HashSet, Hashtable.

**Контракт hashCode():**

1. Если `x.equals(y)` возвращает `true`, то `x.hashCode() == y.hashCode()` **обязательно** должно быть `true`
2. Если `x.equals(y)` возвращает `false`, `x.hashCode()` и `y.hashCode()` **могут** быть одинаковыми или разными (это
   называется "коллизия хешей")

Пример нарушения: переопределили equals() по id, но не переопределили hashCode():

```java
class Lead {
  private String id;
  // ... остальные поля ...

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Lead lead = (Lead) o;
    return Objects.equals(id, lead.id);
  }

  // НЕТ переопределенного hashCode()!
}

Lead lead1 = new Lead("1", "ivan@mail.ru", "+7123", "TechCorp", "NEW");
Lead lead2 = new Lead("1", "ivan@mail.ru", "+7123", "TechCorp", "NEW");

lead1.

equals(lead2);  // true (equals переопределен)
lead1.

hashCode() ==lead2.

hashCode();  // false! (hashCode стандартный, основан на адресе)

// Использование в HashMap
Map<Lead, String> map = new HashMap<>();
map.

put(lead1, "Status A");
map.

get(lead2);  // null! HashMap не нашел объект, потому что hashCode разные
```

HashMap использует hashCode() для быстрого поиска "корзины" (bucket), а затем equals() для точного сравнения объектов в
корзине. Если hashCode() разные, HashMap даже не вызовет equals() — сразу решит что объекты разные.

**Правильная реализация:**

```java

@Override
public int hashCode() {
  return Objects.hash(id);  // Хеш вычисляется по тем же полям, что и equals
}
```

Теперь два Lead с одинаковым id будут иметь одинаковый hashCode:

```java
lead1.hashCode() ==lead2.

hashCode();  // true
map.

get(lead2);  // ⟪§⟫ - HashMap нашел объект!
```

### Инсайты BCORE-3

- всё время забываю, что при вызове метода в классе он ссылается сам на себя

### Результат само-ревью BCORE-3

Ошибок не обнаружено согласно Code Review Checklist раздела.

## BCORE-4: Три способа создания класса + UUID вместо String

### Когда использовать каждый подход

Обычный класс (с конструктором и геттерами вручную) используется для mutable entity — изменяемых сущностей с поведением
и бизнес-логикой. Например, Lead с методами qualify(), reject(), convert() — это не просто данные, а объект с состоянием
и поведением. Обычный класс позволяет полный контроль: можно добавить валидацию в конструктор, сделать поля изменяемыми
через сеттеры, добавить бизнес-методы. Record используется для immutable value objects — неизменяемых объектов-значений
без поведения. Например, Contact (firstName, lastName, email) или Address (city, street, zip) — это просто данные для
передачи между слоями приложения. Record даёт максимальную краткость, гарантирует неизменяемость, автоматически
реализует правильный equals/hashCode. Lombok используется в legacy проектах на старых версиях Java (до 14) или когда
нужны mutable классы с автогенерацией. В новых проектах на Java 17+ предпочтение отдаётся Record как стандарту языка.
Trade-offs: явное против неявного

### Trade-offs: явное против неявного

Каждый подход имеет компромиссы. Обычный класс максимально явный — всё написано руками, легко читать и понимать, но
много кода (30+ строк), легко допустить ошибку, долго писать и поддерживать. Record максимально краткий — одна строка,
компилятор гарантирует правильность, но неизменяемость обязательна (нельзя изменить поле после создания), геттеры без
префикса get (firstName() вместо getFirstName()), ограниченная кастомизация. Lombok балансирует между ними — можно
выбрать что генерировать (@Getter, @EqualsAndHashCode отдельно или @Data всё сразу), поддерживает mutable классы, но
требует внешнюю зависимость (не часть Java), генерация кода "скрыта" (нужно знать что делает аннотация), потенциальные
проблемы с IDE (хотя современные IDE поддерживают Lombok хорошо). Для Backend Core модуля мы выбираем Record как
современный стандарт Java: краткость + безопасность + нет внешних зависимостей.

### Инсайты BCORE-4

- Record очень удобный тип класса

### Результат само-ревью BCORE-4

Ошибок не обнаружено согласно Code Review Checklist раздела.

## BCORE-5: ООП: инкапсуляция и композиция

Q: Когда использовать композицию, а когда наследование?

A: Композицию используйте в 95% случаев — когда переиспользуете код, группируете связанные данные, следуете принципу
единственной ответственности. Lead "имеет" контактные данные → композиция (private Contact contact). Наследование
используйте только для полиморфизма — когда нужен общий интерфейс для разных реализаций. Пример: метод processPayment(
Payment payment) работает с разными типами платежей (CreditCard, PayPal, BankTransfer) через общий интерфейс Payment →
наследование. Проверка: задайте вопрос "является ли A разновидностью B?" Если да — наследование, если нет — композиция.
Lead является Contact? Нет → композиция. Dog является Animal? Да → наследование.

Q: Почему композиция лучше наследования для переиспользования кода?

A: Наследование создаёт жёсткую связь — дочерний класс зависит от всех изменений родительского класса ("хрупкий базовый
класс"). Если Contact добавит метод, Lead унаследует его автоматически, даже если это не нужно. Наследование допускает
только одного родителя в Java (single inheritance), композиция — множество компонентов (Lead содержит Contact, Address,
ActivityLog одновременно). Композиция гибче: можно заменить Contact на EnhancedContact без изменения Lead, при
наследовании пришлось бы менять структуру классов. Классический пример: утка и летает и плавает, куда её поместить в
иерархию Bird → FlyingBird / SwimmingBird? Композиция решает просто: Duck has FlyingAbility, Duck has SwimmingAbility.

Q: Как обращаться к полям через композицию? Не слишком ли длинные цепочки?

A: Доступ через делегацию: ```lead.contact().address().city()``` — три уровня вызовов. Это выглядит длиннее чем
```lead.getCity()```, но делает структуру явной и поддерживает инкапсуляцию. Каждый уровень возвращает объект следующего
уровня. Альтернатива — "сквозные геттеры": добавить в Lead метод ```public String city() { return contact.address()
.city(); }```, чтобы вызывать ```lead.city()``` напрямую. Trade-off: сквозные методы удобнее для клиента, но скрывают
композицию
и добавляют boilerplate (нужен сквозной метод для каждого поля). В учебном модуле используем явную делегацию для
понимания структуры, в enterprise выбор зависит от публичности API.

Q: Что делать если промежуточный объект может быть ```null```?

A: В Sprint 1 гарантируем через валидацию в конструкторе, что Contact и Address не null. Добавляем проверку в компактный
конструктор Record:
```public Lead { if (contact == null) throw new IllegalArgumentException("Contact cannot be null");``` }.
Это делает композицию безопасной — lead.contact().address().city() никогда не бросит NullPointerException. В следующих
модулях (Java Advanced, Spring) изучим Optional для явного выражения "может быть null":
```Optional<Contact> contact()``` и
безопасной навигации: ```lead.contact().map(Contact::address).map(Address::city).orElse("Unknown")```. Для Sprint 1
используем
обязательные поля через валидацию.

Q: Зачем создавать отдельные классы для Contact и Address? Не проще оставить поля в Lead?

A: Отдельные классы дают переиспользование (DRY) и группировку по смыслу (cohesion). До рефакторинга: поля email, phone,
city, street, zip дублируются в Lead, Customer, Employee, Supplier — 4 копии. При добавлении поля (например,
countryCode) нужно изменить 4 класса. После рефакторинга: Contact и Address создаются один раз, используются везде —
изменение в одном месте. Группировка: Address инкапсулирует всё о географии (city, street, zip), Contact — всё о
контактных данных (email, phone, address). Это принцип единственной ответственности (Single Responsibility Principle).
Lead отвечает за бизнес-логику лида (company, status), Contact — за контактную информацию, Address — за географические
данные. Каждый класс имеет одну причину для изменения.

Q: Можно ли использовать композицию с обычными классами или только с Record?

A: Композиция работает с любыми типами классов: обычные классы, Record, даже интерфейсы. Для ```value objects (Contact,
Address)``` предпочтительнее Record — краткость, неизменяемость, автогенерация equals/hashCode. Для mutable entity с
бизнес-логикой используются обычные классы. Например, в Sprint 3 создадим ```class LeadService { private LeadRepository
repository; } ``` — композиция обычного класса с интерфейсом. Композиция — это паттерн организации кода, не зависящий от
конкретного типа класса. Главное: один класс содержит другой как поле (private Contact contact), доступ через
делегацию (getContact()). Record удобен для неизменяемых компонентов композиции, обычные классы — для изменяемых или с
поведением.

### Инсайты BCORE-5

- Композиция используется в 95% случаев.

### Результат само-ревью BCORE-5

Ошибок не обнаружено согласно Code Review Checklist раздела.

## BCORE-6: Repository интерфейс и InMemory реализация

### Пять ключевых навыков Repository Pattern

1. Понимание интерфейса как контракта

Интерфейс Repository определяет обещание предоставить операции с данными без указания как именно эти операции
реализованы. Это фундаментальная концепция абстракции: высокоуровневый код зависит от контракта (интерфейса),
низкоуровневый код предоставляет реализацию. Dependency Inversion Principle гласит - зависимость направлена от деталей к
абстракциям, а не наоборот.

Практический пример: сервис LeadService имеет поле private final Repository<Lead> repository (тип - интерфейс). При
создании сервиса передаем конкретную реализацию: new LeadService(new InMemoryLeadRepository()) для разработки или new
LeadService(new JdbcLeadRepository()) для продакшена. Сервис вызывает repository.findAll() не зная деталей - данные в
памяти, PostgreSQL или Redis. Код сервиса остается стабильным при смене технологий хранения. Это снижает coupling и
повышает testability.

Источники: Oracle - Interfaces, Martin Fowler - Repository Pattern

2. Generic типы для переиспользования кода

Generic типы Repository<T> позволяют создать один интерфейс для разных сущностей: Repository, Repository, Repository.
Параметр T (type parameter) заменяется конкретным классом при использовании. Это type-safe альтернатива Object -
компилятор проверяет типы на этапе компиляции, исключая runtime ошибки ClassCastException.

Современная практика: начинать с конкретного Repository без дженериков для понимания концепции, затем переходить к
Repository для переиспользования. Generic Repository имеет ограничения в сложных сценариях (считается анти-паттерном
когда интерфейс постоянно расширяется новыми методами), но идеален для InMemory реализаций и простых CRUD операций.
Spring Data JPA решает проблемы Generic Repository через CrudRepository с умными методами query by method name.

Источники: Oracle - Generics, Dot Net Tutorials - Generic Repository

3. ArrayList performance characteristics

ArrayList предоставляет fast random access (get/set за O(1)), fast iteration, amortized O(1) добавление в конец. Внутри
использует Object[] массив с automatic resize: capacity увеличивается в 1.5 раза когда заканчивается место. Операции
поиска (contains, indexOf) - O(n) линейные, так как требуют проверки каждого элемента.

Critical tradeoffs для Repository: ArrayList excellent для небольших коллекций (10-100 элементов), simple iteration,
случаев где order важен. Проблемы начинаются при 1000+ элементов: findById становится медленным (O(n) вместо O(1) у
HashMap), дедупликация через contains требует full scan. В BCORE-7 решим через HashSet (O(1) для contains), в BCORE-8
через HashMap (O(1) для get by ID). Правило: до ~5 элементов ArrayList быстрее чем HashMap из-за lower overhead, после
5-6 элементов HashMap выигрывает.

Источники: Baeldung - Collections Time Complexity, Stack Overflow - ArrayList vs HashMap

4. Aggregate Root и границы Repository

Aggregate Root определяет границу транзакции и консистентности в домене. Только корень агрегата получает Repository -
внутренние объекты доступны через навигацию от корня. Lead - Aggregate Root (имеет UUID, управляет жизненным циклом),
Contact и Address - value objects (без самостоятельного ID, immutable, равенство по значениям).

Ошибка новичков: создавать Repository для каждого entity класса - ContactRepository, AddressRepository. Это нарушает
Aggregate boundaries: Contact должен существовать только в контексте Lead, не как standalone entity. Изменения Contact
всегда через Lead: lead.updateContact(newContact), не через отдельный ContactRepository.save(). Это обеспечивает
invariants агрегата - бизнес-правила проверяются на уровне Lead, который контролирует свои внутренние объекты.

Источники: Martin Fowler - DDD Aggregate, Baeldung - Aggregate Root

5. InMemory реализации для тестирования

InMemory Repository - паттерн где данные хранятся в коллекции вместо базы данных. Быстрое создание/уничтожение делает
InMemory идеальным для unit тестов: не нужны Testcontainers, H2, или моки. Тесты проверяют реальное поведение
Repository (дедупликация, порядок, границы) за миллисекунды.

Преимущество перед моками: InMemory Repository содержит реальную логику (дедупликация через contains, навигация через
stream), а моки просто возвращают stub данные. Когда тестируешь LeadService с InMemory repository, проверяешь интеграцию
сервиса с реальным Repository API, не с mockito заглушками. Многие production проекты используют InMemory для тестов:
создают общий интерфейс, тесты работают с интерфейсом, runtime использует JdbcRepository.

---

### Про класс vs record для InMemory Repository

`InMemoryLeadRepository` — **обычный класс**, не record. Это правильный выбор.

|             | Record                     | Обычный класс                      |
|-------------|----------------------------|------------------------------------|
| Состояние   | immutable (финальные поля) | **мутабельное** (список `storage`) |
| Конструктор | канонический (все поля)    | **пустой по умолчанию**            |
| Назначение  | данные (DTO, Value Object) | **сервис/хранилище**               |

**equals/hashCode — не нужны.** `InMemoryLeadRepository` — сервис, его экземпляры не сравнивают и не кладут в HashMap.
`equals` от Object (по ссылке) достаточно.

### Aggregate Root (Lead) + Value Objects (Contact, Address)

Lead — **корень агрегата** (Aggregate Root): имеет UUID, управляет жизненным циклом. Contact и Address — **Value Objects
**: не имеют самостоятельного ID, immutable, доступны только через Lead.

#### Бытовые аналогии

**1. Человек и его паспорт**

Lead — человек (уникальный ID, существует сам по себе). Contact — страница с адресом прописки (не существует без
паспорта). Address — конкретный адрес на странице.

```
Человек (Lead)
  └── Страница с адресом (Contact)
        └── Город, улица, индекс (Address)
```

**2. Телефонная книга**

Контакт — корень. Номер телефона — значение (не существует без контакта). Адрес — значение.

#### Почему это важно

Без этой архитектуры можно создать `ContactRepository` и сохранить `Contact` без `Lead`:

- Contact хранит email лида — без Lead непонятно, кому он принадлежит
- При удалении Lead его Contact останется сиротой
- Изменение Contact через `contactRepository.save()` минует валидацию Lead

#### Схема

```
Repository:          LeadRepository  (только корень!)
                        │
Aggregate Root:       Lead  (UUID, жизненный цикл)
                      / \
Value Objects:   Contact  Address  (без ID, immutable)
                   /
               Address  (вложенный)
```

Граница: всё внутри Lead — целостная единица. Изменение Contact = новый Lead. Удаление Lead = удаление Contact и
Address.

### Почему бизнес-код зависит от интерфейса Repository<T>, а не от InMemoryLeadRepository?

**Dependency Inversion Principle (DIP):** высокоуровневый код не должен зависеть от низкоуровневых реализаций. Оба
должны зависеть от абстракций (интерфейсов).

#### Пример из проекта

Без DIP — жёсткая привязка:

```java
public class LeadService {
  private final InMemoryLeadRepository repository;  // зависит от конкретной реализации
}
```

С DIP — через интерфейс:

```java
public class LeadService {
  private final Repository<Lead> repository;  // зависит от абстракции
}
```

**Что это даёт:**

1. **Замена реализации без правки бизнес-кода**
    - Тесты: `new InMemoryLeadRepository()` — быстро, без БД
    - Production: `new PostgresLeadRepository()` — реальная БД
    - Код `LeadService` при этом **не меняется**

2. **Бытовой пример: зарядка для телефона**

   | Компонент | В IT | Аналогия |
                                                                                                      |---|---|---|
   | Интерфейс | `Repository<T>` | USB-C разъём |
   | Реализация 1 | `InMemoryLeadRepository` | Зарядка от PowerBank |
   | Реализация 2 | `PostgresLeadRepository` | Зарядка от розетки |
   | Потребитель | `LeadService` | Телефон |

   Телефону (сервису) всё равно, откуда идёт энергия — из розетки или PowerBank. Важно, что разъём USB-C (интерфейс)
   один и тот же.

   Аналогично `LeadService` не знает и не должен знать, хранятся лиды в памяти или в PostgreSQL. Он знает только
   контракт: `add`, `remove`, `findById`, `findAll`.

3. **Тестирование**

   ```java
   // Тест использует InMemory — быстро, без БД, без моков
   class LeadServiceTest {
       private Repository<Lead> repository = new InMemoryLeadRepository();
       private LeadService service = new LeadService(repository);

       @Test
       void shouldDoSomething() {
           service.doSomething();
           assertThat(repository.findAll()).hasSize(1);
       }
   }
   ```

   Если бы сервис зависел от `InMemoryLeadRepository`, в тестах нельзя было бы подменить реализацию. С интерфейсом —
   можно.

4. **Что меняется при переключении на БД**

   | Компонент | Меняется | Не меняется |
                                                                                                      |---|---|---|
   | `Repository<T>` | ❌ | ✅ Интерфейс остаётся |
   | `InMemoryLeadRepository` | ✅ Удаляется | ❌ |
   | `PostgresLeadRepository` | ✅ Добавляется | ❌ |
   | **`LeadService`** | ❌ | **✅ Без изменений** |
   | **Тесты** | ❌ | **✅ Продолжают использовать InMemory** |

### Геттеры для storage: internal state vs data holder

`private final List<Lead> storage` — **internal state**. Геттер не нужен. Весь доступ к данным уже предоставлен через
публичные методы `add`, `remove`, `findById`, `findAll`.

**Чем опасен геттер:**

```java
// Без геттера — всё под контролем:
repository.add(lead);          // проверка на null + дубликат
repository.

findAll();           // defensive copy

// С геттером — можно обойти все проверки:
repository.

getStorage().

add(lead);         // null не проверен, дубликат проскочит
repository.

getStorage().

clear();            // все данные потеряны без remove()
```

**Вывод:** `InMemoryLeadRepository` — сервис, не data holder. `findAll()` с defensive copy — единственный правильный
способ доступа.

### Типобезопасность через generics

`Repository<T>` — параметризованный интерфейс. Тип данных фиксируется на этапе компиляции.

**Без generics — ошибка в runtime:**

```java
public interface Repository {
  void add(Object entity);
}

Repository repo = new InMemoryLeadRepository();
repo.

add("not a lead");       // компилируется!
((Lead)repo.

findById(id));   // ClassCastException в runtime
```

**С generics — ошибка при компиляции:**

```java
public interface Repository<T> {
  void add(T entity);
}

Repository<Lead> repo = new InMemoryLeadRepository();
repo.

add("not a lead");       // ❌ не скомпилируется
repo.

add(new Lead(...));      // ✅ только Lead
```

**Что даёт:**

| Без `T`                      | С `Repository<T>`              |
|------------------------------|--------------------------------|
| `Object` entity — любой тип  | `T entity` — только нужный тип |
| Нужен явный каст `(Lead)`    | Каст не нужен                  |
| Ошибка в runtime             | Ошибка **при компиляции**      |
| `findAll()` → `List` (чего?) | `findAll()` → `List<T>`        |

**Аналогия:** парковка без шлагбаума пропустит велосипед (`ClassCastException` при выезде). Парковка `Parking<Car>` —
только машины, шлагбаум на въезде.

## BCORE-7: Set для уникальности — Автоматическая дедупликация

### Когда использовать Set или List

- **Set:** требуется гарантия уникальности элементов (один email может быть только у одного лида), часто проверяете
  наличие элемента через contains (поиск дубликатов перед добавлением), не важен порядок элементов или порядок
  определяется сортировкой (TreeSet), выполняете теоретико-множественные операции (объединение списков без дубликатов,
  пересечение двух наборов данных).
- **List** : допускаются дубликаты (история действий пользователя может содержать повторяющиеся события), важен порядок
  вставки (лента новостей показывается в хронологическом порядке), часто обращаетесь к элементам по индексу get(index) —
  Set не поддерживает индексный доступ, нужна сортировка с дубликатами (рейтинг игроков может иметь одинаковые баллы).

### Set — интерфейс, брат-близнец List

`Set` и `List` — оба наследуют `Collection`, но имеют разный контракт:

```
Iterable
  └── Collection
        ├── List (упорядоченный, с индексами, дубликаты разрешены)
        │     ├── ArrayList
        │     └── LinkedList
        │
        └── Set (без дубликатов, без индексов)
              ├── HashSet (на хеш-таблице, O(1))
              └── TreeSet (отсортированный, O(log n))
```

|                   | `List`       | `Set`                                         |
|-------------------|--------------|-----------------------------------------------|
| Дубликаты         | разрешены    | **запрещены**                                 |
| Порядок           | гарантирован | не гарантирован (кроме LinkedHashSet/TreeSet) |
| Доступ по индексу | `get(0)`     | нет                                           |
| `contains()`      | O(n)         | **O(1)** (HashSet)                            |

**Для `InMemoryLeadRepository`** `Set<Lead>` + `equals/hashCode` по `id` — дедупликация автоматически:

```java
leads.add(lead);  // сам проигнорирует дубликат за O(1), без отдельного contains()
```

### Map — отдельная иерархия (ключ → значение)

`Map` — это **не** `Collection`, а самостоятельный интерфейс. Хранит пары ключ → значение.

```
Map (интерфейс — ключ → значение)
  ├── HashMap (на хеш-таблице, O(1))
  │     └── LinkedHashMap (с сохранением порядка вставки)
  └── TreeMap (отсортированный по ключу, O(log n))
```

| Характеристика    | `Map`                                             |
|-------------------|---------------------------------------------------|
| Хранит            | пары **ключ → значение**                          |
| Уникальность      | **ключи** уникальны, значения могут дублироваться |
| Доступ            | `map.get(key)` — O(1) для HashMap                 |
| `containsKey()`   | O(1) — проверка существования ключа               |
| `containsValue()` | O(n) — полный проход                              |

**Отличие от `Set`:** `Set` хранит только уникальные элементы. `Map` хранит пары, где уникальны ключи, но значения могут
повторяться.

Q5: **В чём разница между HashSet, LinkedHashSet, TreeSet?**

A5: HashSet — произвольный порядок элементов, O(1) для add/contains/remove, использует hashCode/equals. LinkedHashSet —
сохраняет порядок вставки через двусвязный список, O(1) для операций, чуть медленнее HashSet из-за поддержки списка.
TreeSet — сортирует элементы через Comparable или Comparator, O(log n) для операций, элементы должны реализовывать
Comparable. Выбирайте HashSet для максимальной производительности без требований к порядку, LinkedHashSet для сохранения
порядка вставки, TreeSet для автоматической сортировки.

Q6: **Как HashSet обрабатывает null элементы?**

A6: HashSet допускает один null элемент — при добавлении null hashCode не вызывается (NullPointerException), вместо
этого null помещается в специальный бакет с индексом 0. Повторное добавление null возвращает false (дубликат). TreeSet
не допускает null (NullPointerException при добавлении), LinkedHashSet работает как HashSet — один null разрешён.

## BCORE-8: Map для быстрого поиска

### 📚 Теория: от коллекций к ассоциативным массивам

Коллекции `List` и `Set` работают с единичными элементами: `List` сохраняет порядок добавления, `Set`
обеспечивает уникальность. Но обе требуют перебора для поиска конкретного элемента — методы `indexOf()` у
`ArrayList` или `contains()` у `HashSet` внутри проходят по всем элементам до первого совпадения. Временная
сложность таких операций линейная `O(n)`: чем больше элементов, тем дольше поиск.

`Map` решает задачу по-другому: вместо хранения единичных элементов он хранит пары **"ключ → значение"**.
Ключ играет роль уникального идентификатора, а значение — это ссылка на сам объект.
Основная операция `Map` — метод `get(key)`, который возвращает значение по ключу за константное время `O(1)`.
Это значит, поиск в Map с 10 элементами и в Map с миллионом элементов занимает одинаковое время.

**Контракт `Map` интерфейса:**

`Map` не является наследником `Collection`, это отдельная ветка иерархии коллекций:

```
Iterable
  └── Collection
        ├── List
        └── Set

Map (отдельная ветка)
  ├── HashMap
  ├── LinkedHashMap
  └── TreeMap
```

Основные методы контракта:

```java
map.put(key, value)      // добавить пару, вернуть старое значение или null
map.

get(key)             // получить значение по ключу, O(1)
map.

remove(key)          // удалить пару, вернуть удалённое значение
map.

containsKey(key)     // проверить наличие ключа, O(1)
map.

keySet()             // множество всех ключей (Set<K>)
map.

values()             // коллекция всех значений (Collection<V>)
map.

entrySet()           // множество всех пар (Set<Map.Entry<K,V>>)
```

Главная особенность: каждый ключ уникален. Если вызвать `put()` дважды с одним ключом,
второй вызов перезапишет значение.

---

### 🔧 HashMap: устройство внутри

`HashMap` — самая популярная реализация `Map`. Обеспечивает `O(1)` для операций `get`/`put`/`remove`
через механизм хеширования. Внутри `HashMap` — массив из **"корзин" (buckets)**, где каждая корзина
может содержать несколько пар ключ-значение.

**Механика работы HashMap:**

1. **Вычисление индекса:** При `map.put(key, value)` HashMap вызывает `key.hashCode()` и вычисляет
   индекс корзины: `index = hashCode % capacity`, где `capacity` — размер массива корзин (по умолчанию 16).

2. **Размещение в корзине:** HashMap помещает объект `Entry` (пара ключ-значение) в корзину с вычисленным
   индексом. `Entry` хранит ссылку на ключ, значение и следующий элемент (связный список).

3. **Hash collision:** Если два разных ключа дают одинаковый индекс (например `hashCode1 = 17`, `hashCode2 = 33`,
   оба дают `index = 1` при `capacity = 16`), происходит коллизия. HashMap решает коллизии через
   связный список: все элементы с одним индексом хранятся цепочкой.

4. **Поиск значения:** При `map.get(key)` HashMap вычисляет индекс корзины, затем проходит по цепочке
   элементов, сравнивая ключи через `key.equals()`. При хорошей хеш-функции каждая корзина содержит
   0-2 элемента — поиск занимает константное время.

5. **Resizing:** Когда элементов > `capacity × loadFactor` (по умолчанию `16 × 0.75 = 12`), HashMap
   увеличивает `capacity` в 2 раза и пересчитывает индексы всех элементов (rehashing). Дорогая операция
   `O(n)`, но происходит редко.

**Схема работы HashMap:**

```
map.put(key, value)
        │
        ▼
  hashCode = key.hashCode()
        │
        ▼
  index = hashCode % capacity
        │
        ▼
  buckets[index]
        │
        ├── Корзина пуста? ── Да ──► Создать Entry(key, value)
        │                               │
        └── Нет (коллизия!) ────────────► Entry.next = старый элемент
                                          │
                                          └── Entry становится головой списка
```

**Почему HashMap быстрый:** в идеальном случае каждая корзина содержит 0-1 элемент.
Тогда `get()` выполняет всего 2 операции: вычислить индекс и взять элемент из массива — `O(1)`.
В худшем случае (все ключи дают один hashCode) HashMap деградирует до связного списка с `O(n)`,
но на практике `UUID.randomUUID()` даёт отличное распределение.

---

### 📦 Entry интерфейс: хранение пар ключ-значение

Каждая пара в Map представлена внутренним интерфейсом `Map.Entry<K, V>`:

```java
K getKey()        // получить ключ этой пары

V getValue()      // получить значение этой пары

V setValue(V v)   // изменить значение, вернуть старое
```

**entrySet vs keySet — производительность:**

```java
// Медленнее: каждый get() ищет в HashMap заново
for(String leadId :leadMap.

keySet()){
Lead lead = leadMap.get(leadId);   // O(1), но лишний вызов
}

// Быстрее: Entry уже содержит ключ и значение
    for(
Map.Entry<String, Lead> entry :leadMap.

entrySet()){
String id = entry.getKey();        // прямой доступ
Lead lead = entry.getValue();      // без повторного поиска
}
```

---

### ⚡ Performance O(1): константное время

Главное преимущество `HashMap` — константное время для базовых операций.

| Операция             | `ArrayList`                  | `HashSet`              | `HashMap`              |
|----------------------|------------------------------|------------------------|------------------------|
| Поиск элемента       | `O(n)` — `stream().filter()` | `O(1)` — `contains()`  | `O(1)` — `get(key)`    |
| Добавление           | `O(1)` — `add()`             | `O(1)` — `add()`       | `O(1)` — `put()`       |
| Удаление по значению | `O(n)` — `remove(obj)`       | `O(1)` — `remove(obj)` | `O(1)` — `remove(key)` |
| Итерация             | `O(n)`                       | `O(n)`                 | `O(n)` — `entrySet()`  |

**Практический пример:** CRM с 100 000 лидов, менеджер открывает карточку по id:

- `ArrayList`: `stream().filter(...).findFirst()` — проверит в среднем 50 000 элементов
- `HashSet`: аналогично 50 000 проверок (Set не поддерживает поиск по произвольному полю)
- **`HashMap`**: `map.get(targetId)` — вычислит hashCode, найдёт корзину, сделает 1-2 сравнения equals()

Разница между `O(n)` и `O(1)` критична при частых операциях.

---

### 🎯 Выбор коллекции по задаче

**Когда использовать `List`:**

- Нужен порядок добавления (история сообщений, лента новостей)
- Допускаются дубликаты (лог событий, список задач)
- Нужен доступ по индексу `list.get(5)`

**Когда использовать `Set`:**

- Нужна автоматическая дедупликация (роли пользователя, уникальные email)
- Порядок не важен (`HashSet`) или нужна сортировка (`TreeSet`)
- Нужна быстрая проверка наличия `set.contains(element)` за `O(1)`

**Когда использовать `Map`:**

- Нужен быстрый поиск по ключу `map.get(id)` за `O(1)`
- Данные имеют естественный идентификатор (id, email, username)
- Нужно хранить связь "идентификатор → объект"

**Комбинирование коллекций — реальный пример:**

```java
class LeadRepository {
  private Map<UUID, Lead> leadsById = new HashMap<>();   // быстрый поиск по id
  private List<Lead> leadsByDate = new ArrayList<>();     // порядок добавления
  private Set<String> uniqueEmails = new HashSet<>();     // проверка дубликатов

  public void save(Lead lead) {
    if (uniqueEmails.contains(lead.contact().email())) {
      throw new IllegalArgumentException("Email уже существует");
    }
    leadsById.put(lead.id(), lead);
    leadsByDate.add(lead);
    uniqueEmails.add(lead.contact().email());
  }

  public Lead findById(UUID id) {
    return leadsById.get(id);  // O(1)
  }

  public List<Lead> findRecent(int limit) {
    return leadsByDate.stream().limit(limit).toList();
  }
}
```

---

### 🔗 Связь Map с equals/hashCode контрактом

HashMap использует те же методы `equals()` и `hashCode()`, что и `HashSet` (BCORE-3).

**Почему контракт критичен для Map:**

1. **Поиск корзины:** `HashMap` вызывает `key.hashCode()` для вычисления индекса. Если `hashCode()`
   нестабилен, HashMap не найдёт элемент.
2. **Сравнение ключей:** Внутри корзины сравнение через `key.equals()`. Без переопределённого `equals()`
   логически равные ключи считаются разными.
3. **Перезапись значения:** `map.put(key, value)` с существующим ключом должен найти старую пару
   через `equals()` и заменить значение.

**Безопасные ключи:** `String`, `UUID`, `Integer`, `Long`, `Enum`, `Record` — immutable,
с правильным `equals`/`hashCode`.

**Небезопасные ключи:** Mutable объекты (изменение поля после добавления в Map → потеря элемента),
классы без `equals`/`hashCode` (дубликаты вместо перезаписи).

**Правило:** используй immutable объекты или классы с правильным `equals`/`hashCode`.
Никогда не изменяй объект-ключ после добавления в `Map`.

---

### 🏛️ SRP и границы Repository

Бизнес-правило CRM: "один email = один лид". Возникает соблазн добавить проверку email в
`save()` репозитория. Но это нарушит **Single Responsibility Principle**.

Repository имеет одну ответственность — **хранение данных**:

- `save(lead)` → сохранить лид по UUID
- `findById(uuid)` → найти лид за O(1)

Если добавить в Repository проверку бизнес-правила "уникальность email", он начинает делать две
вещи: хранить данные **И** применять бизнес-логику. При изменении правила ("теперь уникальность
по email+phone") придётся менять Repository. При добавлении новых правил Repository станет монстром.

**Где должна быть бизнес-логика?** В `LeadService` (Sprint 5). Сервис использует `LeadRepository`
для хранения, но сначала проверяет все бизнес-правила. Repository остаётся простым и быстрым,
Service содержит логику. Это разделение — ключевой принцип Clean Architecture.

---

### 🛠️ Ключевые навыки работы с Map

**1. Базовые операции:**

```java
Map<UUID, Lead> leadMap = new HashMap<>();

Contact contact = new Contact("ivan@mail.ru", "+7123",
    new Address("Moscow", "Tverskaya 1", "101000"));
Lead lead = new Lead(leadId, contact, "TechCorp", "NEW");
leadMap.

put(lead.id(),lead);  // Ключ = UUID, значение = ссылка на объект

Lead found = leadMap.get(leadId);           // объект или null
boolean exists = leadMap.containsKey(leadId);  // true
Lead removed = leadMap.remove(leadId);         // удалённый объект
```

**2. Обработка отсутствующих значений:**

```java
// getOrDefault — значение по умолчанию
Lead lead = leadMap.getOrDefault(unknownId, createDefaultLead());

// computeIfAbsent — создать если ключа нет (лямбда вызовется только при отсутствии)
Lead computed = leadMap.computeIfAbsent(newId, id -> {
  return new Lead(id, defaultContact, "Unknown", "NEW");
});
```

**3. Итерация через Map:**

```java
// Только ключи
for(UUID leadId :leadMap.

keySet()){
    System.out.

println("Lead ID: "+leadId);
}

// Только значения
    for(
Lead lead :leadMap.

values()){
    System.out.

println("Email: "+lead.contact().

email());
    }

// Пары ключ-значение (РЕКОМЕНДУЕТСЯ)
    for(
Map.Entry<UUID, Lead> entry :leadMap.

entrySet()){
UUID id = entry.getKey();
Lead lead = entry.getValue();
}
```

**4. put vs replace:**

```java
// put — добавит или перезапишет
leadMap.put(leadId, updatedLead);

// replace — изменит только если ключ существует
Lead old = leadMap.replace(leadId, updatedLead);  // вернёт старое или null

// replace с проверкой старого значения (атомарно)
boolean ok = leadMap.replace(leadId, oldLead, newLead);
```

**5. Stream API с Map:**

```java
// Найти лидов со статусом NEW
List<Lead> newLeads = leadMap.values().stream()
        .filter(l -> l.status().equals("NEW"))
        .toList();

// Отфильтровать Map
Map<UUID, Lead> active = leadMap.entrySet().stream()
    .filter(e -> !e.getValue().status().equals("CLOSED"))
    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

// Преобразовать значения
Map<UUID, String> emailMap = leadMap.entrySet().stream()
    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().contact().email()));
```

## BCORE-9: Создаём LeadService для бизнес-логики

Checkpoint 1: LeadService создан

    [X] Класс в пакете ru.mentee.power.crm.service
    [X] Конструктор принимает LeadRepository (не создаёт внутри!)
    [X] Поле repository объявлено как private final

Checkpoint 2: Метод addLead работает

    [X] Проверяет существование email через repository.findByEmail()
    [X] Выбрасывает IllegalStateException при дубликате
    [X] Создаёт и сохраняет Lead при уникальном email

Checkpoint 3: Repository очищен

    [X] Метод save() не содержит проверки дубликатов
    [X] Repository только хранит данные

Checkpoint 4: Тесты написаны

    [X] Минимум 5 тестов в LeadServiceTest
    [X] Тест на успешное создание лида
    [X] Тест на исключение при дубликате email
    [X] Coverage ≥80%

## BCORE-10: Внедрение зависимостей через конструктор

Checkpoint 1: Mockito добавлен

    [X] build.gradle содержит mockito-core
    [X] build.gradle содержит mockito-junit-jupiter
    [X] Ctrl+Ctrl → gradle build проходит

Checkpoint 2: Mock тесты работают

    [X] @Mock аннотация на LeadRepository
    [X] @ExtendWith(MockitoExtension.class) на классе теста
    [X] when().thenReturn() настраивает поведение
    [X] verify() проверяет вызовы

Checkpoint 3: Понимание DI

    [X] Могу объяснить разницу BAD vs GOOD
    [X] Понимаю зачем нужен mock
    [X] Понимаю что такое IoC

### Сравнение: new внутри vs DI через конструктор

#### BAD: new InMemoryLeadRepository() внутри класса

```java
public class LeadService {
  // Тесная связанность!
  private final LeadRepository repository = new InMemoryLeadRepository();
}
```

#### GOOD: DI через конструктор

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

## BCORE-11: HelloCrmServer — HTTP-обработка запросов

### HelloHandler: стандартное vs проектное

HTTP-обработчик — метод, вызываемый при каждом входящем запросе.
Весь код внутри делится на **стандартную обвязку** (~70%) и **проектную логику** (~30%).

#### Стандартная обвязка (пишется одинаково в любом проекте)

```java

@Override
public void handle(HttpExchange exchange) throws IOException {

  exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
  exchange.sendResponseHeaders(200, response.getBytes().length);

  OutputStream os = exchange.getResponseBody();
  os.write(response.getBytes());
  os.close();

  exchange.close();
}
```

| Строка                                | Что делает                        | Меняется? |
|---------------------------------------|-----------------------------------|-----------|
| `implements HttpHandler`              | Контракт «умею обрабатывать HTTP» | ❌         |
| `getResponseHeaders().set(...)`       | Заголовки (тип контента)          | Значение  |
| `sendResponseHeaders(код, длина)`     | Статус-код (200/404/500)          | Код       |
| `OutputStream os = getResponseBody()` | Открыть канал ответа              | ❌         |
| `os.write(...)`                       | Отправить байты клиенту           | ❌         |
| `os.close()`                          | Закрыть поток                     | ❌         |

#### Проектная логика (меняется под задачу)

```java
String method = exchange.getRequestMethod();      // GET, POST, PUT, DELETE
String path = exchange.getRequestURI().getPath();  // "/api/leads", "/api/leads/123"

String response;
if("GET".

equals(method) &&"/api/leads".

equals(path)){
List<Lead> leads = leadService.findAll();     // бизнес-логика
response =

toJson(leads);

}else if("POST".

equals(method) &&path.

startsWith("/api/leads")){
String body = new String(exchange.getRequestBody().readAllBytes());
Lead lead = parseLead(body);                   // парсинг
response =

toJson(lead);

}else{
    exchange.

sendResponseHeaders(404,-1);         // не найдено
    return;
        }
```

| Что меняется                          | Как                                              |
|---------------------------------------|--------------------------------------------------|
| Путь (`/api/leads`, `/api/leads/123`) | Маршруты — какие URL обрабатывает сервер         |
| Метод (GET/POST/PUT/DELETE)           | CRUD операции                                    |
| Чтение тела (`getRequestBody()`)      | POST/PUT приносят данные для создания/обновления |
| Контент ответа                        | HTML-заглушка → JSON с лидами                    |

#### Бытовые аналогии

| HTTP                | Аналогия                                                         |
|---------------------|------------------------------------------------------------------|
| Стандартная обвязка | Почтальон всегда стучит, отдаёт посылку, просит подпись          |
| Проектная логика    | «Какая посылка и куда нести» — зависит от адресата и содержимого |
| `Content-Type`      | Наклейка «Хрупкое» — получатель знает, как обращаться            |
| 200 / 404 / 500     | «Доставлено» / «Адресат не найден» / «Посылка разбилась»         |

## BCORE-12: Первый Servlet для списка лидов

### Связка Main ↔ LeadListServlet через ServletContext

Приложение держится на трёх «мостиках»: **ServletContext** (общая память), **ключ `"leadService"`**
(как найти объект), и **Tomcat** (кто маршрутизирует запросы).

#### Main.java — старт (ПОЛОЖИЛИ)

```java
Context ctx = tomcat.addContext("", new File(".").getAbsolutePath());  // ① создать контекст
ctx.

getServletContext().

setAttribute("leadService",service);          // ② положить LeadService
tomcat.

addServlet(ctx, "LeadListServlet",new LeadListServlet());      // ③ зарегистрировать сервлет
    ctx.

addServletMappingDecoded("/leads","LeadListServlet");             // ④ путь → сервлет
```

- ① `addContext` — создаёт контекст веб-приложения
- ② `setAttribute("leadService", ...)` — кладёт `LeadService` в `ServletContext` под ключом `"leadService"`
- ③ `addServlet` — регистрирует экземпляр сервлета в том же контексте
- ④ `addServletMappingDecoded` — `/leads` → `LeadListServlet.doGet()`

#### LeadListServlet.java — запрос (ДОСТАЛИ)

```java
ServletContext context = getServletContext();                              // ⑤ получить контекст
LeadService service = (LeadService) context.getAttribute("leadService");  // ⑥ достать сервис
List<Lead> leads = service.findAll();                                     // ⑦ бизнес-логика
```

- ⑤ `getServletContext()` — возвращает **тот же** объект, в который Main положил сервис
- ⑥ `getAttribute("leadService")` — достаёт по ключу, ключ должен совпадать (регистр важен!)
- ⑦ `findAll()` — реальная бизнес-логика через DI-сервис

#### Поток HTTP-запроса

```
Браузер → GET /leads → Tomcat
                          │
                          ▼ Томкат смотрит маппинг: "/leads" → "LeadListServlet"
                          │
                          ▼ Вызывает LeadListServlet.doGet(request, response)
                          │
                          ▼ getServletContext() → getAttribute("leadService")
                          │
                          ▼ LeadService.findAll() → List<Lead>
                          │
                          ▼ Генерация HTML-таблицы → response.getWriter().println(...)
                          │
                          ▼ Браузер ← 200 OK + HTML
```

Контекст — это общая «память» приложения. **Main кладёт объект при старте, сервлеты достают при каждом запросе.** Один
объект `LeadService` на всё приложение.

#### Ключ `"leadService"`: почему регистр важен

```java
// Main.java:
ctx.getServletContext().

setAttribute("LeadService",service);  // ← большая L

// LeadListServlet.java:
context.

getAttribute("leadService");  // ← маленькая l → null → IllegalStateException!
```

`"LeadService"` ≠ `"leadService"`. Если ключи не совпадают посимвольно — сервлет не найдёт сервис.

## BCORE-13: Подключаем JTE шаблонизатор + Tailwind CSS

### Рефакторинг: от сырого HTML к шаблонам

**Было (BCORE-12):** 35 строк `writer.println("<html>...")` — Java и HTML смешаны в коде.

**Стало (JTE):** сервлет только готовит данные, рендеринг делегирован в `.jte` файлы.

#### Что изменилось в LeadListServlet

```java
// ─── init(): подготовка шаблонного движка ОДИН раз при старте ───
@Override
public void init() {
  Path templatePath = Path.of("src/main/jte");
  // Сканер шаблонов: читает .jte файлы из указанной папки при старте
  DirectoryCodeResolver codeResolver = new DirectoryCodeResolver(templatePath);
  // Создать движок: указать откуда брать шаблоны и тип контента (Html)
  this.templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
}

// ─── doGet(): подготовка данных + делегация рендеринга ───
@Override
protected void doGet(...) {
  List<Lead> leads = service.findAll();              // ① бизнес-логика

  Map<String, Object> model = new HashMap<>();        // ② упаковать данные в модель
  model.put("leads", leads);                         //    ключ "leads" → список

  templateEngine.render(                             // ③ делегировать рендеринг
      "leads/list.jte", model,
      new WriterOutput(response.getWriter())
  );
}
```

#### Структура шаблонов

**`layout/main.jte`** — макет страницы (общий header/footer для всех страниц):

```jte
@param gg.jte.Content content     // параметр: контент, который вставится в макет

<!DOCTYPE html>
<html>
<head><script src="tailwindcss"></script></head>
<body>
    <header>CRM System</header>
    <main>${content}</main>        // ← сюда подставится содержимое из list.jte
    <footer>&copy; 2025 CRM</footer>
</body></html>
```

**`leads/list.jte`** — только контент (таблица с лидами):

```jte
@param List<Lead> leads            // данные из модели

@template.layout.main(content = @`  // «оберни меня в main.jte»
    <table>
        @for(var lead : leads)      // цикл прямо в шаблоне
            <tr>
                <td>${lead.email()}</td>    // интерполяция: ${...} вместо + ...
                <td>${lead.company()}</td>
                <td>${lead.status()}</td>
            </tr>
        @endfor
    </table>
`)
```

#### Поток вызова

```
GET /leads → LeadListServlet.doGet()
  ├─ ① service.findAll() → List<Lead>
  ├─ ② model.put("leads", leads)
  └─ ③ templateEngine.render("leads/list.jte", model, output)
        ├─ list.jte → @template.layout.main(content = ...)
        │     └─ main.jte → <header> + ${content} + <footer>
        │           └─ ${content} = таблица из list.jte
        └─ WriterOutput → response.getWriter() → браузер
```

#### Сравнение

| Аспект             | Было (ручной HTML)                  | Стало (JTE)                                      |
|--------------------|-------------------------------------|--------------------------------------------------|
| Где HTML           | Строки в `writer.println()`         | Файлы `.jte` — отдельно от Java                  |
| Циклы              | `for` в Java                        | `@for` в шаблоне                                 |
| Вывод значения     | `"<td>" + lead.email() + "</td>"`   | `${lead.email()}` — интерполяция                 |
| Header/Footer      | Дублировался в каждом сервлете      | Один `main.jte` для всех                         |
| Строк в сервлете   | ~35 строк HTML                      | **1 строка**: `templateEngine.render(...)`       |
| Инициализация      | Не нужна                            | `init()` — `TemplateEngine.create(...)` один раз |
| Дизайнер           | ❌ Надо знать Java                   | ✅ Только HTML/Tailwind                           |
| Производительность | `.println()` — I/O на каждую строку | JTE предкомпилирует шаблон → быстрее             |

## BCORE-14: Переходим на Spring Boot

### Точка входа

```java

@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
```

Одна аннотация `@SpringBootApplication` заменяет три: `@Configuration` + `@ComponentScan` + `@EnableAutoConfiguration`.
Одна строка `SpringApplication.run()` запускает весь процесс ниже.

### Процесс запуска: 7 этапов

Когда вызывается `SpringApplication.run()`, происходит многоступенчатый процесс:

1. **Создание ApplicationContext** — Spring IoC контейнер (пустой «мешок» для бинов)
2. **Загрузка конфигурации** — `application.yml` / `application.properties` (порт, профили, настройки БД)
3. **Auto-configuration** — `@ConditionalOnClass` / `@ConditionalOnBean` определяют, какие бины создавать (наличие
   `starter-web` в classpath → авто-создание Tomcat, `DispatcherServlet`, Jackson)
4. **Component Scanning** — `@ComponentScan` находит `@Service`, `@Repository`, `@Controller` и регистрирует их как бины
5. **Dependency Injection** — Spring связывает бины через конструкторы, `@Autowired`, `@Value`
6. **Запуск embedded сервера** — `ServletWebServerApplicationContext` создаёт Tomcat, регистрирует `DispatcherServlet`,
   стартует на порту (обычно `:8080` или указанном в настройках)
7. **ApplicationReadyEvent** — приложение готово принимать HTTP-запросы (2–5 секунд от старта)

### 7 этапов подробно

**① Создание ApplicationContext.**

Spring создаёт **пустой контейнер** — объект, который будет хранить все бины. Пока внутри ничего нет. Это как пустой
склад: стеллажи готовы, но товаров ещё не завезли.

```java
// Внутри SpringApplication.run() происходит примерно это:
ApplicationContext context = new AnnotationConfigApplicationContext();
// Пока пустой — ни одного бина. Сейчас начнём заполнять.
```

Тип контекста зависит от приложения: для web — `ServletWebServerApplicationContext` (со встроенным Tomcat), для batch —
другой, для reactive — третий. Spring Boot сам выбирает нужный по наличию библиотек в classpath.

**② Загрузка application.yml.**

Spring читает `src/main/resources/application.yml`. Находит настройки и запоминает их в объекте `Environment`.

Твой `application.yml`:

```yaml
server:
  port: 8081
spring:
  application:
    name: mentee-power-crm
```

Что происходит внутри:

```java
// Spring парсит YAML и кладёт в Environment:
environment.getProperty("server.port");           // → "8081"
environment.

getProperty("spring.application.name"); // → "mentee-power-crm"
```

Потом, когда очередь дойдёт до запуска Tomcat, тот спросит: «Какой порт?» → `environment.getProperty("server.port")` →
`8081`. Никакого хардкода `tomcat.setPort(8081)` — всё из внешнего файла.

**③ Auto-configuration.**

Spring сканирует **classpath** и смотрит, какие библиотеки подключены. Для каждой найденной применяет правила из
`spring-boot-autoconfigure`.

Твой случай: в `build.gradle` есть `spring-boot-starter-web`. Это тянет за собой Tomcat, Jackson, Spring MVC. Spring
видит их в classpath и думает:

```
classpath содержит:
  ✅ jakarta.servlet.Servlet.class        → нужен веб-сервер
  ✅ org.apache.catalina.startup.Tomcat   → встроенный Tomcat
  ✅ com.fasterxml.jackson.databind       → Jackson для JSON

Результат авто-конфигурации:
  → Создать бин TomcatServletWebServerFactory (фабрика Tomcat)
  → Создать бин DispatcherServlet (точка входа для всех HTTP-запросов)
  → Создать бин ObjectMapper (Jackson — сериализация Java → JSON)
```

Как это решается внутри:

```java

@Configuration
@ConditionalOnClass({Servlet.class, Tomcat.class})
@ConditionalOnMissingBean(EmbeddedWebServerFactory.class)
public class EmbeddedWebServerAutoConfiguration {
  @Bean
  public TomcatServletWebServerFactory tomcatFactory() {
    return new TomcatServletWebServerFactory();  // Spring создал сам
  }
}
```

Ты не писал `new Tomcat()`, не настраивал порт, не регистрировал `DispatcherServlet`. Spring Boot сделал это сам, потому
что увидел `starter-web` в зависимостях.

**④ Component Scanning.**

Spring сканирует все классы в пакете `ru.mentee.power.crm` и подпакетах. Ищет аннотации-стереотипы:

| Аннотация         | Что это                 | Пример из проекта        |
|-------------------|-------------------------|--------------------------|
| `@Service`        | Бизнес-логика           | `LeadService`            |
| `@Repository`     | Доступ к данным         | `InMemoryLeadRepository` |
| `@RestController` | HTTP-обработчики (JSON) | `LeadController`         |
| `@Component`      | Общий бин               | Любой utility-класс      |

Что происходит:

```
Сканирование пакета ru.mentee.power.crm...
  ├── spring/Application.java              → @SpringBootApplication (точка входа)
  ├── spring/controller/LeadController.java → @RestController → СОЗДАТЬ БИН "leadController"
  ├── service/LeadService.java             → без @Service → НЕ создавать
  └── repository/InMemoryLeadRepository.java → без @Repository → НЕ создавать
```

На этом этапе `LeadController` регистрируется как бин (потому что `@RestController` включает `@Component`).
`LeadService` и `InMemoryLeadRepository` — пока нет.

**⑤ Dependency Injection.**

Spring обходит все созданные бины и смотрит: «кому что нужно для работы?» Если у бина конструктор с параметрами — Spring
ищет подходящий бин для каждого параметра и передаёт.

Сейчас `LeadController` пустой — нет конструктора с параметрами, нечего внедрять. Но когда появится:

```java

@RestController
public class LeadController {
  private final LeadService service;

  public LeadController(LeadService service) {  // Spring видит: «нужен LeadService!»
    this.service = service;                    // ищет бин в контексте → внедряет
  }
}
```

Если `LeadService` помечен `@Service` — Spring создаст его первым, а потом передаст в `LeadController`. Если аннотации
нет — ошибка: «No qualifying bean of type LeadService».

**⑥ Запуск embedded сервера.**

`ServletWebServerApplicationContext` создаёт экземпляр Tomcat прямо внутри JVM (не отдельный процесс):

```java
// Внутри Spring Boot (упрощённо):
Tomcat tomcat = new Tomcat();
tomcat.

setPort(environment.getProperty("server.port", 8080)); // → 8081 из application.yml
    tomcat.

getServer().

start();
```

Параллельно регистрируется `DispatcherServlet` — центральный сервлет, который принимает ВСЕ HTTP-запросы и
маршрутизирует их по контроллерам (`@GetMapping`, `@PostMapping`).

Порт **8081** (из `application.yml`), а не 8080. Поэтому ручной Tomcat из `Main.java` на 8080 и Spring Boot на 8081
работают одновременно без конфликта.

**⑦ ApplicationReadyEvent.**

Финальный этап. Spring публикует событие `ApplicationReadyEvent` — сигнал «всё готово, можно работать». В консоли
появляется:

```
Started Application in 2.5 seconds
```

С этого момента все HTTP-запросы обрабатываются: `DispatcherServlet` → нужный контроллер → ответ.

### Общая схема запуска SpringApplication.run()

```
SpringApplication.run()
    │
    ▼
① Создание пустого ApplicationContext
    │
    ▼
② Загрузка application.yml → Environment (порт, имя приложения)
    │
    ▼
③ Auto-configuration: classpath scan
    │
    ├── Найден spring-boot-starter-web
    │     ├── Создать TomcatServletWebServerFactory
    │     ├── Создать DispatcherServlet
    │     └── Создать Jackson ObjectMapper
    │
    ▼
④ Component Scanning: поиск @RestController, @Service, @Repository
    │
    ▼
⑤ Dependency Injection: связывание бинов через конструкторы
    │
    ▼
⑥ Запуск Embedded Tomcat на порту из application.yml (8081)
    │
    ▼
⑦ ApplicationReadyEvent → ГОТОВО принимать HTTP-запросы
```

### Что это даёт vs ручной Main.java

|                       | Ручной Main.java (BCORE-13)                | Spring Boot (BCORE-14)                        |
|-----------------------|--------------------------------------------|-----------------------------------------------|
| Создание Tomcat       | `new Tomcat(); tomcat.setPort(8080)`       | Автоматически — один `@SpringBootApplication` |
| Регистрация сервлетов | `tomcat.addServlet(...)` вручную           | Автоматически — `@WebServlet` сканируется     |
| Создание бинов        | `new InMemoryLeadRepository()` в Main.java | `@Repository` → Spring сам создаст и внедрит  |
| Конфигурация          | Хардкод в Java                             | `application.yml` — внешний файл              |
| Строк в точке входа   | ~30 строк Main.java                        | **3 строки** Application.java                 |

## BCORE-15: Первый контроллер — от сервлета к Spring MVC

### Что изменилось с BCORE-14

BCORE-14 — каркас: `@SpringBootApplication` + `application.yml` + бины. Сервер запускается, но HTTP-запросы обрабатывать
некому.

BCORE-15 добавляет контроллер и ViewResolver:

|                 | BCORE-14 (скелет)                | BCORE-15 (контроллер)                       |
|-----------------|----------------------------------|---------------------------------------------|
| HTTP-запросы    | Whitelabel Error Page на все URL | `/leads` → HTML-таблица с лидами            |
| Контроллер      | Пустая заглушка                  | `@Controller` с `@GetMapping("/leads")`     |
| View            | Не было                          | JTE ViewResolver → `leads/list.jte`         |
| Тестовые данные | Нет                              | `@Bean CommandLineRunner` наполняет 5 лидов |
| Сервер          | Запускается                      | Запускается и ОТВЕЧАЕТ                      |

### Путь HTTP-запроса: от браузера до JTE-шаблона

Когда браузер открывает `http://localhost:8081/leads`, цепочка из 10 шагов:

```
Браузер: GET /leads
    │
    ▼
DispatcherServlet (авто-создан Spring Boot)
    │  «принимаю ВСЕ HTTP-запросы, ищу кому делегировать»
    ▼
HandlerMapping
    │  «есть @GetMapping("/leads")?»
    │  «✅ LeadController.showLeads()»
    ▼
LeadController.showLeads(Model model)
    │
    ├─ ① leadService.findAll()              // LeadController.java:22
    │     └─ repository.findAll()             // LeadService.java:38
    │          └─ HashMap.values() → List     // InMemoryLeadRepository.java:42
    │
    ├─ ② model.addAttribute("leads", list)  // LeadController.java:23
    │     └─ ключ "leads" → данные для JTE
    │
    └─ ③ return "leads/list"                // LeadController.java:24
          │  «это логическое имя view, не путь к файлу!»
          ▼
JteViewResolver (из jte-spring-boot-starter-4)
    │  «"leads/list" → "leads/list.jte"»
    │  «загрузить файл из src/main/jte/»
    ▼
JTE Template: leads/list.jte
    │  @for(var lead : leads)
    │  <td>${lead.email()}</td>
    │  ...
    │  ${content} → layout/main.jte (header/footer)
    ▼
200 OK + HTML → Браузер
```

#### По шагам с привязкой к коду

| Шаг | Где                    | Код                                  | Что делает                                 |
|-----|------------------------|--------------------------------------|--------------------------------------------|
| ①   | Браузер → Tomcat       | `GET /leads`                         | HTTP-запрос на порт 8081                   |
| ②   | `DispatcherServlet`    | авто-создан Spring Boot              | Принимает **все** запросы, ищет обработчик |
| ③   | `HandlerMapping`       | ищет `@GetMapping("/leads")`         | Находит `LeadController.showLeads`         |
| ④   | `LeadController:20-21` | `@GetMapping("/leads")`              | Маппинг: метод обрабатывает GET `/leads`   |
| ⑤   | `LeadController:22`    | `leadService.findAll()`              | Делегация бизнес-логики в сервис           |
| ⑥   | `LeadController:23`    | `model.addAttribute("leads", leads)` | Упаковка данных: ключ "leads" → список     |
| ⑦   | `LeadController:24`    | `return "leads/list"`                | Логическое имя view (НЕ путь к файлу!)     |
| ⑧   | `JteViewResolver`      | `"leads/list"` → `leads/list.jte`    | Преобразует имя в физический файл          |
| ⑨   | Рендеринг JTE          | `@for(var lead : leads)`             | Цикл по модели, генерация таблицы          |
| ⑩   | Ответ браузеру         | `200 OK` + `<html>...</html>`        | Готовая страница с таблицей лидов          |

#### Почему return "leads/list" а не return "leads/list.jte"

`ViewResolver` сам добавляет суффикс `.jte` (настроен в `application.yml`: `suffix: .jte`). Контроллер не знает, какой
шаблонизатор используется — JTE, Thymeleaf, JSP. Он возвращает логическое имя, а ViewResolver превращает его в
физический файл. Заменишь шаблонизатор — перепишешь только конфиг, не контроллер.

### Ключевые концепции

**DispatcherServlet.** Главный контроллер Spring MVC, принимает все HTTP-запросы и делегирует их обработку другим
компонентам. Аналог паттерна Front Controller. Регистрируется автоматически через `@SpringBootApplication`, не нужно
настраивать вручную. Источник: Spring MVC Reference Documentation.

**@GetMapping.** Аннотация для маппинга HTTP GET-запросов на метод контроллера. Упрощённая версия
`@RequestMapping(method = RequestMethod.GET)`. `@GetMapping("/leads")` означает: этот метод обрабатывает GET-запросы на
URL `/leads`. Источник: Spring Framework Annotations Guide.

**Model.addAttribute.** Метод для добавления данных в модель, которые будут доступны в view. Первый параметр — ключ (
String), второй — значение (Object). В JTE-шаблоне к атрибуту обращаются через `@param` с тем же именем:
`@param List<Lead> leads`. Источник: Spring MVC Model API Documentation.

**ViewResolver.** Компонент Spring MVC, преобразующий логическое имя view (`"leads/list"`) в физический ресурс (файл
`leads/list.jte`). Для JTE используется `JteViewResolver` (из `jte-spring-boot-starter-4`), для Thymeleaf —
`ThymeleafViewResolver`, для JSP — `InternalResourceViewResolver`. Источник: Spring Boot View Technologies Guide.

### @Bean в Application.java — как Spring создаёт бин из метода

В отличие от `@Service`/`@Repository` (где Spring сам создаёт объект класса), `@Bean` на методе говорит Spring: «вызови
ЭТОТ метод — то, что он вернёт, станет бином».

**Шаг 1: объявление бина**

```java

@Bean
CommandLineRunner seedLeads(LeadService service) {
  return args -> {
    for (int i = 0; i < 5; i++) {
      service.addLead("email" + i + "@mail.ru", "+7900" + i, "Company #" + i, LeadStatus.NEW);
    }
  };
}
```

Разбор строки:

| Часть                    | Что значит                                                                                                      |
|--------------------------|-----------------------------------------------------------------------------------------------------------------|
| `@Bean`                  | «Spring, создай объект по этому рецепту и положи в контекст»                                                    |
| `CommandLineRunner`      | **Тип возврата** — функциональный интерфейс с методом `run(String... args)`                                     |
| `seedLeads`              | **Имя метода** → станет именем бина: `"seedLeads"`                                                              |
| `(LeadService service)`  | **Параметр** — Spring видит: «нужен LeadService» → ищет в контексте → находит `@Service LeadService` → передаёт |
| `return args -> { ... }` | Возвращает **лямбду** — реализацию `CommandLineRunner`                                                          |

**Шаг 2: как Spring обрабатывает**

```
Spring видит @Bean на seedLeads
    │
    ├── «Какие параметры у метода?» → (LeadService service)
    │     └── Есть бин LeadService в контексте? → ✅ @Service → да, есть
    │     └── Передаю: seedLeads(leadService)
    │
    ├── «Что возвращает метод?» → лямбду args -> { ... }
    │     └── Тип: CommandLineRunner
    │
    └── Кладу лямбду в контекст как бин "seedLeads"
```

**Шаг 3: когда выполняется**

`CommandLineRunner` — специальный интерфейс. Spring вызывает `run(args)` у **всех** бинов этого типа сразу после
создания контекста, но до приёма HTTP-запросов:

```
SpringApplication.run()
  → созданы все бины
  → ApplicationContext готов
  → вызов всех CommandLineRunner.run(args)
       └── seedLeads.run(args) → 5 × service.addLead(...)
  → Embedded Tomcat стартует
  → ApplicationReadyEvent
```

**`args` в лямбде — это НЕ `String[] args` из main.** `SpringApplication.run()` пробрасывает аргументы командной строки
во все `CommandLineRunner`-ы. Если запускаешь `java -jar app.jar --debug` → `args = ["--debug"]`. При обычном запуске →
`args = []` (пустой массив).

### Инициализация данных: где это делать

`@Bean CommandLineRunner` — идиоматичный путь для сидирования тестовых данных в Spring Boot. Альтернативы:

| Подход                                        | Когда                                                     |
|-----------------------------------------------|-----------------------------------------------------------|
| `CommandLineRunner`                           | Тестовые данные при разработке, миграции БД               |
| `ApplicationRunner`                           | То же, но с парсингом аргументов в `ApplicationArguments` |
| `@PostConstruct` на `@Configuration`          | Одноразовая инициализация без доступа к `args`            |
| `@EventListener(ApplicationReadyEvent.class)` | Действия ПОСЛЕ полного старта (нужен готовый сервер)      |

## BCORE-16: Сравнение стеков — Servlet vs Spring Boot

### Результаты тестов (StackComparisonTest)

| Метрика | Servlet (Main.java) | Spring Boot (Application.java) |
|---|---|---|
| **Время старта** | 414 ms | 2 497 ms |
| **Разница** | — | ~6× медленнее |
| **Строк кода** | ~40 (Main.java) | ~10 (Application.java + config) |
| **Строк в точке входа** | ~25 строк (ручной Tomcat) | **3 строки** (`@SpringBootApplication` + `run()`) |
| **Tomcat создание** | `new Tomcat()`, `setPort()`, `addContext()`, `addServlet()` — вручную | **Автоматически** (auto-config) |
| **ViewResolver** | Вручную в `LeadListServlet.init()` | **Автоматически** (JTE Starter) |
| **DI** | `new InMemoryLeadRepository()` + `new LeadService()` в Main.java | `@Service` + `@Repository` → Spring свяжет сам |
| **Порт** | `8080` (хардкод) | `8081` (`application.yml`) |
| **Запуск** | `./gradlew run` | `./gradlew bootRun` |

### Trade-offs

| | Servlet стек | Spring Boot |
|---|---|---|
| ✅ Быстрый старт | 414ms — почти мгновенно | 2.5s — приемлемо для деплоя |
| ✅ Скорость разработки | Каждая строка — ручной код | Convention over Configuration |
| ✅ Явный контроль | Видно всё: каждый бин, каждый порт | «Магия» авто-конфигурации |
| ❌ Медленнее старт | — | ~6× медленнее (одноразово при деплое) |
| ❌ Много boilerplate | ~25 строк настройки Tomcat | 3 строки |

### Вывод

Servlet стек — **простота и скорость старта** (414ms). Spring Boot — **convention over configuration** (2.5s). На практике Spring Boot побеждает: 2.5 секунды старта — один раз при деплое, а экономия десятков строк boilerplate — каждый день. Spring Boot «из коробки» даёт health checks, метрики, внешнюю конфигурацию — всё это пришлось бы писать вручную в Servlet стеке.

### Как запустить сравнение

```bash
# Terminal 1: Servlet стек
./gradlew run

# Terminal 2: Spring Boot
./gradlew bootRun

# Terminal 3: тест
./gradlew test --tests "ru.mentee.power.crm.StackComparisonTest"
```

---

## Code Review Checklist

### Функциональность

- [X] Код полностью решает поставленную задачу (./gradlew clean checkstyleMain test jacocoTestCoverageVerification
  build)
- [X] Обработаны граничные случаи (null, пустые коллекции, экстремальные значения)
- [X] Ошибки обрабатываются корректно

### Тесты

- [X] Добавлены тесты для нового функционала
- [X] Все тесты проходят локально (`./gradlew test`)
- [X] Покрыты позитивные и негативные сценарии
- [X] Проверены граничные случаи
- [X] JaCoCo coverage ≥ 80% для нового кода

### Читаемость и стиль

- [X] Имена переменных, методов и классов понятные и отражают назначение
- [X] Нет дублирования кода (DRY principle)
- [X] Код легко читается
- [X] Checkstyle проходит (`./gradlew checkstyleMain`)
- [X] Нет закомментированного кода или отладочного вывода (`System.out.println`)

### Документация

- [X] README обновлён (если добавлена новая функциональность)
- [ ] Публичные методы имеют JavaDoc (если применимо)
- [X] Примеры использования актуальны
- [ ] Runbook обновлён (если изменились команды запуска/проверки)

### Производительность и безопасность

- [X] Нет очевидных проблем производительности
- [X] Нет хардкода паролей, токенов или конфиденциальных данных

---

## Личный глоссарий терминов Dev Tools

### Структура глоссария

Каждый термин содержит:

- **RU / EN** — русское и английское название
- **Определение** — краткое (1-2 предложения) объяснение термина
- **Контекст использования** — где и когда применяется
- **Пример** — конкретное применение в коде/команде/документации
- **Источник** — ссылка на официальную документацию

### Категория: Enterprise Application

---

#### CRM (Customer Relationship Management)

**Определение:** A CRM system is software designed to manage a company's interactions with current and potential
customers, including storing data, tracking communication, and automating sales processes.

**Контекст использования:** CRM (например, Salesforce, HubSpot) используется для управления клиентской базой,
отслеживания сделок, взаимодействий и воронки продаж.

**Пример:** Менеджер добавляет нового клиента в CRM, фиксирует звонок и переводит сделку на следующий этап воронки.

**Источник:** https://www.salesforce.com/crm/what-is-crm/

---

#### ERP (Enterprise Resource Planning)

**Определение:** An ERP system is software that integrates core business processes such as finance, inventory, and
operations into a unified system.

**Контекст использования:** ERP используется для централизованного хранения и обработки данных разных отделов через
единый backend и базу данных.

**Пример:** Разработчик реализует сервис для обработки заказов, который обновляет складские остатки и финансовые записи.

**Источник:** https://www.sap.com/products/erp/what-is-erp.html

---

#### DMS (Document Management System)

**Определение:** A DMS is software used to store, manage, and track electronic documents.

**Контекст использования:** DMS используется для хранения файлов, версионирования и управления доступом к документам.

**Пример:** Разработчик реализует загрузку файлов, хранение в S3 и контроль доступа к документам.

**Источник:** https://www.ibm.com/topics/document-management

---

## Прочая информация

### Почему нужен каст (приведение типа)

У Java — **статическая типизация**. Компилятор решает, какие поля и методы доступны, глядя на **тип переменной**, а не
на реальный объект.

```java
public boolean equals(Object o) {  // o типа Object, хотя реально там Lead
```

Параметр `o` объявлен как `Object`. Даже если ты передал `Lead`, компилятор всё равно смотрит на `Object`. А у `Object`
нет поля `id`.

```java
o.id           // ❌ Ошибка: у Object нет поля id
o.

getEmail()   // ❌ Ошибка: у Object нет метода getEmail()
o.

getClass()   // ✅ работает — метод есть у Object
```

Каст говорит компилятору: *«Я знаю больше тебя, поверь — тут реально Lead, дай мне доступ»*:

```java
Lead lead = (Lead) o;  // ты: компилятор, это Lead, дай пройти
lead.id               // ✅ теперь видно
lead.

getEmail()       // ✅ теперь видно
```

#### Аналогия

В реальной жизни: к тебе подходит человек в капюшоне. Ты видишь только силуэт (тип `Object`). Нельзя сказать «эй,
человек, покажи ID-карту» — ты не уверен, кто это.

Проверил — это точно коллега (`getClass() == Lead.class`). Теперь можешь попросить показать бейдж (`lead.id`).

```
o силуэт ──> проверка ──> точно Lead ──> каст ──> lead.id
(Object)     (getClass)    (уверен)     (Lead)   (доступно)
```

#### Что будет без каста

Даже если `o` реально `Lead` с заполненным `id`, ты **физически не можешь** написать `o.id` — компилятор скажет «cannot
find symbol id». Чтобы вызывать методы/поля класса, переменная должна быть этого типа. Каст — мост между "компилятор
знает `Object`" и "на самом деле `Lead`".

---

### Программирование на уровень интерфейса: `List` vs `ArrayList`

```java
List<Lead> storage = new ArrayList<>();    // ✅ гибко (program to an interface)
ArrayList<Lead> storage = new ArrayList<>(); // ❌ жёсткая привязка к реализации
```

`List` — **интерфейс** (контракт: какие методы есть у списка). `ArrayList`, `LinkedList` — **реализации**.

**Почему `List` лучше:**

- Можно заменить `ArrayList` на `LinkedList` без правки кода, меняя только `new...()`
- Переменная видит только методы из `List` — класс не зависит от специфики `ArrayList`
- Стандартный паттерн в Java коллекциях и фреймворках (Spring, Hibernate)

**Иерархия:**

```
Iterable → Collection → List (интерфейс)
                        ├── ArrayList (на массиве)
                        ├── LinkedList (на узлах)
                        └── Vector (устаревший)
```

**Метафора:** `List` — меню (описывает блюда), `ArrayList` — конкретное блюдо (как приготовлено). Клиент заказывает по
меню, а не требует конкретного повара.

---

### Практики кода (стиля)

#### boolean

Имена boolean-переменных — вопрос (состояние), на который отвечает true/false. Префиксы: is, has, was, should, can.

---

### Лямбда-выражения (λ)

**Лямбда** — анонимная функция, которую можно передать как аргумент.
Вместо «создай класс с одним методом» — пишешь одну строку.

#### Синтаксис

```java
(параметры)->{тело }
// или коротко (одно действие):
    (параметры)->результат
```

#### Примеры из проекта

```java
// 1. Предикат для фильтрации:
leads.stream()
    .

filter(lead ->lead.

email().

equals(email))  // «каждый lead проверь на email»
    .

findFirst();

// 2. Mockito — динамический ответ:
when(repo.findByEmail(anyString()))
    .

thenAnswer(inv ->inv.

getArgument(0));  // «верни то, что передали»

// 3. Действие, если есть значение:
    optional.

ifPresent(lead ->System.out.

println(lead.email()));

// 4. Поставщик исключения:
    .

orElseThrow(() ->new

RuntimeException("Not found"));
```

#### Бытовые аналогии

| Лямбда                      | Аналогия                                                          |
|-----------------------------|-------------------------------------------------------------------|
| `lead -> lead.email()`      | Инструкция курьеру: «у каждой посылки проверь адрес»              |
| `() -> new Lead(...)`       | Конверт с запасным ключом: «если основной потерян — сделай новый» |
| `inv -> inv.getArgument(0)` | Автоответчик: «что скажешь, то и повторю»                         |

#### Главное

Лямбда — это **поведение, упакованное в переменную**. Ты не говоришь «как делать»
(отдельный класс, implements), ты говоришь **«что делать»** — прямо в месте вызова.

---

### Convention over Configuration — «Соглашение вместо настройки»

Принцип: фреймворк **сам догадывается** о конфигурации, если ты следуешь общепринятым правилам. Конфигурация нужна
только при отклонении от стандарта.

**Пример из проекта:**

| Ты написал                                     | Фреймворк сделал САМ                   |
|------------------------------------------------|----------------------------------------|
| `@Test` над методом                            | JUnit запустит его как тест            |
| `@Mock` над полем                              | Mockito создаст мок                    |
| DI-конструктор `LeadService(LeadRepository r)` | Spring сам найдёт и внедрит реализацию |
| `@BeforeEach`                                  | Выполнит перед каждым тестом           |

**Без COC (явная конфигурация, Spring XML 2005):**

```xml

<bean id="leadService" class="ru.mentee.crm.service.LeadService">
    <constructor-arg ref="leadRepository"/>    <!-- вручную -->
</bean>
<bean id="leadRepository" class="ru.mentee.crm.repository.InMemoryLeadRepository"/>
```

**С COC (Spring Boot, сегодня):**

```java

@Service
public class LeadService {
  private final LeadRepository repository;

  public LeadService(LeadRepository r) {
    this.repository = r;
  }
}
// Spring САМ найдёт LeadRepository и внедрит — без XML и конфигурации
```

**Аналогия:** в ресторане без COC — ты заказываешь воду, хлеб, салфетку. С COC — садишься за столик, официант уже знает:
вода без газа, приборы справа. 90% гостей довольны без лишних слов.




