package ru.mentee.power.crm.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HelloCrmServerTest {

  private HelloCrmServer server;
  private int port;

  @BeforeEach
  void setUp() throws Exception {
    server = new HelloCrmServer(0);  // случайный свободный порт
    server.start();
    port = server.getPort();
  }

  @AfterEach
  void tearDown() {
    server.stop();
  }

  @Test
  @DisplayName("Should respond 200 with HTML on /hello path")
  void shouldRespond200ToHello() throws Exception {
    URL url = new URL("http://localhost:" + port + "/hello");
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");

    assertThat(conn.getResponseCode()).isEqualTo(200);
    assertThat(conn.getContentType()).contains("text/html");

    String body = new String(conn.getInputStream().readAllBytes());
    assertThat(body).contains("Hello CRM!");
    assertThat(body).contains("<!DOCTYPE html>");
  }

  @Test
  @DisplayName("Should respond 404 on unknown path")
  void shouldRespond404OnUnknownPath() throws Exception {
    URL url = new URL("http://localhost:" + port + "/unknown");
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");

    assertThat(conn.getResponseCode()).isEqualTo(404);
  }

  @Test
  @DisplayName("Should handle multiple requests without failing")
  void shouldHandleMultipleRequests() throws Exception {
    for (int i = 0; i < 3; i++) {
      URL url = new URL("http://localhost:" + port + "/hello");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");

      assertThat(conn.getResponseCode()).isEqualTo(200);
    }
  }

  @Test
  @DisplayName("Should reject connections after stop")
  void shouldRejectConnectionsAfterStop() throws Exception {
    server.stop();
    URL url = new URL("http://localhost:" + port + "/hello");

    assertThatThrownBy(() -> {
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.connect();
    }).isInstanceOf(ConnectException.class);
  }
}
