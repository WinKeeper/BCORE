package ru.mentee.power.crm.servlet;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import gg.jte.TemplateEngine;
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

  @Mock
  private TemplateEngine templateEngine;

  private LeadListServlet servlet;

  @BeforeEach
  void setUp() {
    servlet = new LeadListServlet() {
      @Override
      public ServletContext getServletContext() {
        return servletContext;
      }
    };
    servlet.setTemplateEngine(templateEngine);
  }

  @Test
  @DisplayName("Should render template with leads")
  void shouldRenderTemplateWithLeads() throws Exception {
    Lead firstLead = new Lead(UUID.randomUUID(), "ivan@mail.ru",
        "+7123", "TechCorp", LeadStatus.NEW);
    Lead secondLead = new Lead(UUID.randomUUID(), "olga@mail.ru",
        "+7456", "StartupLab", LeadStatus.CONTACTED);

    when(servletContext.getAttribute("leadService")).thenReturn(leadService);
    when(leadService.findAll()).thenReturn(List.of(firstLead, secondLead));

    servlet.doGet(request, response);

    verify(templateEngine).render(
        eq("leads/list.jte"),
        argThat(model -> model.containsKey("leads")
            && ((List<?>) model.get("leads")).size() == 2),
        any()
    );
  }

  @Test
  @DisplayName("Should render template with empty list")
  void shouldRenderTemplateWithEmptyList() throws Exception {
    when(servletContext.getAttribute("leadService")).thenReturn(leadService);
    when(leadService.findAll()).thenReturn(List.of());

    servlet.doGet(request, response);

    verify(templateEngine).render(
        eq("leads/list.jte"),
        argThat(model -> model.containsKey("leads")
            && ((List<?>) model.get("leads")).isEmpty()),
        any()
    );
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
