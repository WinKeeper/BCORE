package ru.mentee.power.crm;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Интеграционный тест сравнения Servlet и Spring Boot стеков.
 * ЗАПУСТИТЬ СЕРВЕРЫ ВРУЧНУЮ ПЕРЕД ТЕСТОМ:
 * Terminal 1: ./gradlew run          (Servlet на 8080)
 * Terminal 2: ./gradlew bootRun      (Spring Boot на 8081)
 */
class StackComparisonTest {

  @SpringBootApplication
  public static class MinimalSpringApp {
  }

  private static final int SERVLET_PORT = 8080;
  private static final int SPRING_PORT = 8081;

  private final HttpClient httpClient = HttpClient.newHttpClient();

  private int countTableRows(String html) {
    return (int) java.util.regex.Pattern.compile("<tr>")
        .matcher(html)
        .results()
        .count();
  }

  private long measureServletStartup() throws Exception {
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(0);
    tomcat.addContext("", new File(".").getAbsolutePath());
    tomcat.getConnector();

    long start = System.nanoTime();
    tomcat.start();
    long duration = System.nanoTime() - start;

    tomcat.stop();
    return duration;
  }

  private long measureSpringBootStartup() {
    long start = System.nanoTime();
    ConfigurableApplicationContext ctx = SpringApplication.run(
        MinimalSpringApp.class, new String[]{"--server.port=0"});
    long duration = System.nanoTime() - start;
    ctx.close();
    return duration;
  }

  @Test
  void bothStacksShouldReturnLeadsInHtmlTable() throws Exception {
    HttpRequest servletRequest = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:" + SERVLET_PORT + "/leads"))
        .GET()
        .build();

    HttpRequest springRequest = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:" + SPRING_PORT + "/leads"))
        .GET()
        .build();

    HttpResponse<String> servletResponse = httpClient.send(
        servletRequest, HttpResponse.BodyHandlers.ofString());
    HttpResponse<String> springResponse = httpClient.send(
        springRequest, HttpResponse.BodyHandlers.ofString());

    assertThat(servletResponse.statusCode()).isEqualTo(200);
    assertThat(springResponse.statusCode()).isEqualTo(200);

    assertThat(servletResponse.body()).contains("<table");
    assertThat(springResponse.body()).contains("<table");

    int servletRows = countTableRows(servletResponse.body());
    int springRows = countTableRows(springResponse.body());

    assertThat(servletRows)
        .as("Количество лидов должно совпадать")
        .isEqualTo(springRows);

    System.out.printf("Servlet: %d rows, Spring: %d rows%n", servletRows, springRows);
  }

  @Test
  void shouldMeasureStartupTime() throws Exception {
    long servletStartupNs = measureServletStartup();
    long springStartupNs = measureSpringBootStartup();

    long servletStartupMs = servletStartupNs / 1_000_000;
    long springStartupMs = springStartupNs / 1_000_000;

    System.out.println("=== Сравнение времени старта ===");
    System.out.printf("Servlet стек: %d ms%n", servletStartupMs);
    System.out.printf("Spring Boot: %d ms%n", springStartupMs);
    System.out.printf("Разница: Spring %s на %d ms%n",
        springStartupMs > servletStartupMs ? "медленнее" : "быстрее",
        Math.abs(springStartupMs - servletStartupMs));

    assertThat(servletStartupMs).isLessThan(10_000);
    assertThat(springStartupMs).isLessThan(60_000);
  }
}
