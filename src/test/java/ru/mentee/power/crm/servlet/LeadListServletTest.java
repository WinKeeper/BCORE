package ru.mentee.power.crm.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

@ExtendWith(MockitoExtension.class)
class LeadListServletTest {

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private ServletContext servletContext;

  @Mock
  private LeadService leadService;

  private LeadListServlet servlet;
  private StringWriter responseWriter;

  @BeforeEach
  void setUp() {
    servlet = new LeadListServlet() {
      @Override
      public ServletContext getServletContext() {
        return servletContext;
      }
    };
    responseWriter = new StringWriter();
  }

  @Test
  @DisplayName("Should return HTML table with leads")
  void shouldReturnHtmlTableWithLeads() throws Exception {
    Lead firstLead = new Lead(UUID.randomUUID(), "ivan@mail.ru",
        "+7123", "TechCorp", LeadStatus.NEW);
    Lead secondLead = new Lead(UUID.randomUUID(), "olga@mail.ru",
        "+7456", "StartupLab", LeadStatus.CONTACTED);

    when(servletContext.getAttribute("leadService")).thenReturn(leadService);
    when(leadService.findAll()).thenReturn(List.of(firstLead, secondLead));
    when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

    servlet.doGet(request, response);

    String html = responseWriter.toString();
    assertThat(html).contains("<h1>Lead List</h1>");
    assertThat(html).contains("<td>ivan@mail.ru</td>");
    assertThat(html).contains("<td>TechCorp</td>");
    assertThat(html).contains("<td>NEW</td>");
    assertThat(html).contains("<td>olga@mail.ru</td>");
    assertThat(html).contains("<td>StartupLab</td>");
    assertThat(html).contains("<td>CONTACTED</td>");
    assertThat(html).contains("</table>");
  }

  @Test
  @DisplayName("Should return empty table when no leads")
  void shouldReturnEmptyTableWhenNoLeads() throws Exception {
    when(servletContext.getAttribute("leadService")).thenReturn(leadService);
    when(leadService.findAll()).thenReturn(List.of());
    when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

    servlet.doGet(request, response);

    String html = responseWriter.toString();
    assertThat(html).contains("<h1>Lead List</h1>");
    assertThat(html).contains("<tbody>");
    assertThat(html).contains("</tbody>");
    assertThat(html).doesNotContain("<td>");
  }

  @Test
  @DisplayName("Should throw when LeadService not found in context")
  void shouldThrowWhenServiceNotFound() {
    when(servletContext.getAttribute("leadService")).thenReturn(null);

    assertThatThrownBy(() -> servlet.doGet(request, response))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("LeadService not found");
  }
}
