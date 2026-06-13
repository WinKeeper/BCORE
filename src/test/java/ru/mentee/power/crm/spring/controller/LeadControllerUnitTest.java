package ru.mentee.power.crm.spring.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.spring.service.MockLeadService;

class LeadControllerUnitTest {

  @Test
  void shouldReturnControllerWithoutSprint() {
    // Given: mock service без Spring контейнера
    MockLeadService mockService = new MockLeadService();

    // When: создаём контроллер через конструктор (pure Java)
    LeadController controller = new LeadController(mockService);

    // Then: контроллер работает, использует mock service
    String response = controller.home();
    assertThat(response).contains("2 leads");
  }

  @Test
  void shouldUseInjectionService() {
    // Given
    MockLeadService mockService = new MockLeadService();
    LeadController controller = new LeadController(mockService);

    // When: вызываем метод контроллера
    String response = controller.home();

    // Then: сервис использован (не null)
    assertThat(response).isNotNull();
    assertThat(response).contains("Spring Boot CRM is running");
  }
}
