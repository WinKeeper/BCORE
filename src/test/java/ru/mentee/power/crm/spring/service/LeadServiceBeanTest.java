package ru.mentee.power.crm.spring.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import ru.mentee.power.crm.repository.LeadRepository;

@SpringBootTest
public class LeadServiceBeanTest {
  // Given
  // Ссылка на самого себя ApplicationContext уже создан для доступа к bean
  @Autowired
  private ApplicationContext context;

  //When
  @Test
  @DisplayName("Should create LeadService bean in ApplicationContext")
  void shouldCreateLeadServiceBean() {
    LeadService service = context.getBean(LeadService.class);
    // Then
    assertThat(service).isNotNull();
  }

  //When
  @Test
  @DisplayName("Should create LeadRepository bean in ApplicationContext")
  void shouldCreateLeadRepositoryBean() {
    LeadRepository repository = context.getBean(LeadRepository.class);
    // Then
    assertThat(repository).isNotNull();
  }

  //When
  @Test
  @DisplayName("Should inject LeadRepository into LeadService via DI")
  void shouldInjectLeadRepositoryIntoService() {
    LeadService service = context.getBean(LeadService.class);
    // Then
    assertThat(service.findAll()).isNotNull();
  }
}
