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
