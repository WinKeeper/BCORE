package ru.mentee.power.crm.spring;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private LeadRepository<Lead> repository;

  private final RestTemplate restTemplate = new RestTemplate();

  @Test
  @DisplayName("Should serve leads page with title Lead List")
  void shouldServeLeadsPage() {
    String url = "http://localhost:" + port + "/leads";
    String body = restTemplate.getForEntity(url, String.class).getBody();

    assertThat(body).contains("Lead List");
  }

  @Test
  @DisplayName("Should contain seeded leads in the response")
  void shouldContainSeededLeads() {
    String url = "http://localhost:" + port + "/leads";
    String body = restTemplate.getForEntity(url, String.class).getBody();

    assertThat(body).contains("NEW0@mail.ru");
    assertThat(body).contains("CONTACTED0@mail.ru");
  }

  @Test
  @DisplayName("Should pre-select correct status in edit form")
  void shouldPreSelectStatusInEditForm() {
    Lead lead = new Lead(UUID.randomUUID(), "test@edit.test", "+7999", "T", LeadStatus.QUALIFIED);
    repository.save(lead);

    String body = restTemplate.getForEntity(
        "http://localhost:" + port + "/leads/" + lead.id() + "/edit",
        String.class).getBody();

    assertThat(body).contains("value=\"QUALIFIED\" selected");
    assertThat(body).doesNotContain("value=\"NEW\" selected");
    assertThat(body).doesNotContain("value=\"CONTACTED\" selected");
    assertThat(body).doesNotContain("value=\"CONVERTED\" selected");
  }
}
