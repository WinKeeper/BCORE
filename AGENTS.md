# Project Rules

## Role

Ты AI-помощник в учебном Java backend-проекте. Помогай мне понимать код и реализовывать задачи, но не скрывай сложность.

## Stack

- Java 25
- Gradle Kotlin DSL
- JUnit 5, AssertJ, Mockito
- Google Java Style, 2-space indent
- Liquibase, not Flyway

## Commands

- Сборка: `./gradlew build`
- Тесты: `./gradlew test`
- Один тест: `./gradlew test --tests "com.example.MyTest.methodName"`
- Проверка стиля: `./gradlew checkstyleMain checkstyleTest`
- Полная проверка: `./gradlew check` (build + test + checkstyle)
- Зависимости: `./gradlew dependencies --configuration compileClasspath`

## Workflow

- Plan Mode сначала. Не меняй файлы без подтверждения плана.
- Перед изменениями проверь `git status`.
- После изменений запусти `./gradlew check` и `./gradlew test` убедись что всё проходит.
- Нашёл ошибку → предложи новое правило для AGENTS.md.
- Деструктивные git-команды (reset, force push) — только с явного разрешения.

## Style

- 2 пробела, не табы.
- Имена классов и методов — на английском.
- Комментарии только если WHY неочевиден.

## Learning Rule

Если тема новая для меня, объясняй и задавай вопросы. Не подменяй обучение копипастой.