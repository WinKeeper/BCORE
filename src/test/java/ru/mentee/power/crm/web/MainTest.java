package ru.mentee.power.crm.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MainTest {

  @Test
  @DisplayName("Should start Tomcat and serve leads list on /leads")
  void shouldStartTomcatAndServeLeads() throws Exception {
    Thread serverThread = new Thread(() -> {
      try {
        Main.main();
      } catch (Exception e) {
        // expected when tomcat is stopped or thread interrupted
      }
    });
    serverThread.setDaemon(true);
    serverThread.start();

    // ждать пока Tomcat поднимется (до 10 секунд)
    HttpURLConnection conn = null;
    for (int attempt = 0; attempt < 20; attempt++) {
      Thread.sleep(500);
      try {
        URL url = new URL("http://localhost:8080/leads");
        conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
          break;
        }
      } catch (java.net.ConnectException e) {
        // сервер ещё не готов, пробуем снова
        conn = null;
      }
    }

    assertThat(conn).isNotNull();
    assertThat(conn.getResponseCode()).isEqualTo(200);

    String body = new String(conn.getInputStream().readAllBytes());
    assertThat(body).contains("Lead List");
    assertThat(body).contains("Email0@mail.ru");
    assertThat(body).contains("Email1@mail.ru");

    serverThread.interrupt();
  }
}
