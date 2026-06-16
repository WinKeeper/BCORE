package ru.mentee.power.crm.spring.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;

class LeadControllerTest {

  private MockMvc mockMvc;
  private LeadService leadService;

  @BeforeEach
  void setUp() {
    leadService = mock(LeadService.class);
    mockMvc = MockMvcBuilders.standaloneSetup(new LeadController(leadService)).build();
  }

  @Test
  @DisplayName("Should return leads list view with all leads")
  void shouldReturnLeadsListView() throws Exception {
    when(leadService.findAll()).thenReturn(List.of(
        new Lead(UUID.randomUUID(), "a@b.com", "+7123", "Corp", LeadStatus.NEW),
        new Lead(UUID.randomUUID(), "c@d.com", "+7456", "Inc", LeadStatus.NEW)
    ));

    mockMvc.perform(get("/leads"))
        .andExpect(status().isOk())
        .andExpect(view().name("leads/list"))
        .andExpect(model().attributeExists("leads"))
        .andExpect(model().attribute("leads", hasSize(2)));
  }

  @Test
  @DisplayName("Should create lead and redirect to leads list")
  void shouldCreateLeadAndRedirectToLeadsList() throws Exception {
    mockMvc.perform(post("/leads")
            .param("email", "new@example.com")
            .param("phone", "+71234567890")
            .param("company", "NewCorp")
            .param("status", "NEW"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/leads"));

    verify(leadService).addLead(any());
  }

  @Test
  @DisplayName("Should handle empty leads list")
  void shouldHandleEmptyList() throws Exception {
    when(leadService.findAll()).thenReturn(List.of());

    mockMvc.perform(get("/leads"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("leads", hasSize(0)));
  }

  @Test
  @DisplayName("Should update lead")
  void shouldUpdateLeadWhenUpdateCalls() throws Exception {
    // Given urlTemplate with contextPath and model
    Lead lead = new Lead(UUID.randomUUID(), "mail1@mail.ru", "123123", "TechCorp", LeadStatus.NEW);
    mockMvc.perform(post("/leads/{id}", lead.id())
            .param("email", lead.email())
            .param("phone", lead.phone())
            .param("company", lead.company())
            .param("status", lead.status().toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/leads"));

    // Then
    verify(leadService).updateLead(eq(lead.id()), any(Lead.class));

  }

  @Test
  @DisplayName("Should show edit form with existing lead data")
  void shouldReturnCorrectStateWhen() throws Exception {
    // Given
    UUID id = UUID.randomUUID();
    Lead existing = new Lead(id, "email1@mail.ru", "123123", "Corp", LeadStatus.NEW);

    // When
    when(leadService.findById(id)).thenReturn(Optional.of(existing));

    // Then
    mockMvc.perform(get("/leads/{id}/edit", id))
        .andExpect(status().isOk())
        .andExpect(view().name("spring/edit")) // то что возвращает showEditForm
        .andExpect(model().attributeExists("lead"))
        .andExpect(model().attribute("lead", existing));
  }

  @Test
  @DisplayName("Should return 404 for non existing lead")
  void shouldReturn404WhenLeadNotFound() throws Exception {
    // Given
    UUID id = UUID.randomUUID();

    // When
    when(leadService.findById(id)).thenReturn(Optional.empty());

    // Then
    mockMvc.perform(get("/leads/{id}/edit", id))
        .andExpect(status().is4xxClientError());
  }

}
