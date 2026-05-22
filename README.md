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
- [X] LeadTest создан с минимум 5 тестами
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
