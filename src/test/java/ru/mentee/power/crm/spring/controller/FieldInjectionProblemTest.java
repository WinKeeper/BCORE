package ru.mentee.power.crm.spring.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.spring.service.MockLeadService;

class FieldInjectionProblemTest {

  @Test
  @DisplayName("Should throw NPE when @Autowired field is null outside Spring")
  void fieldInjectionCausesNullPointerWithoutSpring() throws Exception {
    // Spring не создавал этот объект → @Autowired не отработал
    DemoController controller = new DemoController(null);

    java.lang.reflect.Field field = DemoController.class.getDeclaredField("fieldRepository");
    field.setAccessible(true);

    // Поле существует, но равно null
    assertThat(field.get(controller)).isNull();

    // Попытка использовать null-поле → NullPointerException
    assertThatThrownBy(() -> field.get(controller).toString())
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should leave @Autowired field null when created outside Spring container")
  void shouldDemonstrateFieldInjectionFailsWithoutSpring() {
    MockLeadService mockService = new MockLeadService();
    DemoController controller = new DemoController(mockService);

    // fieldRepository остался null — Spring не внедрил (нет контейнера)
    String result = controller.demo();

    // Constructor Injection работает без Spring, Field Injection — нет
    assertThat(result)
        .contains("Constructor Injection (final): ✓ Injected")
        .contains("Field Injection (@Autowired field): ✗ NULL")
        .contains("Setter Injection (@Autowired setter): ✗ NULL");
  }
}
