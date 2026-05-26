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


