package ru.mentee.power.crm.spring;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest {

  @LocalServerPort
  private int port;

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
}
