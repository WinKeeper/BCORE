package ru.mentee.power.crm.spring.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;

class LeadControllerTest {

  private MockMvc mockMvc;
  private LeadService leadService;

  @BeforeEach
  void setUp() {
    leadService = mock(LeadService.class);
    mockMvc = MockMvcBuilders
        .standaloneSetup(new LeadController(leadService))
        .setConversionService(new FormattingConversionService())
        .setValidator(new LocalValidatorFactoryBean())
        .build();
  }

  @Test
  @DisplayName("Should show leads list view with all leads")
  void shouldShowLeadsListView() throws Exception {
    when(leadService.findLeads(null, null)).thenReturn(List.of(
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
  @DisplayName("Should create lead and redirect to list")
  void shouldCreateLeadAndRedirect() throws Exception {
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
  @DisplayName("Should show empty leads list")
  void shouldShowEmptyLeadsList() throws Exception {
    when(leadService.findLeads(null, null)).thenReturn(List.of());

    mockMvc.perform(get("/leads"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("leads", hasSize(0)));
  }

  @Test
  @DisplayName("Should update lead and redirect to list")
  void shouldUpdateLeadAndRedirect() throws Exception {
    Lead lead = new Lead(UUID.randomUUID(), "mail1@mail.ru", "123123", "TechCorp", LeadStatus.NEW);
    mockMvc.perform(post("/leads/{id}", lead.id())
            .param("email", lead.email())
            .param("phone", lead.phone())
            .param("company", lead.company())
            .param("status", lead.status().toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/leads"));

    verify(leadService).updateLead(eq(lead.id()), any(Lead.class));
  }

  @Test
  @DisplayName("Should pre-fill edit form with existing lead data including status")
  void shouldPreFillEditFormWithLeadData() throws Exception {
    UUID id = UUID.randomUUID();
    Lead existing = new Lead(id, "email1@mail.ru", "123123", "Corp", LeadStatus.NEW);

    when(leadService.findById(id)).thenReturn(Optional.of(existing));

    mockMvc.perform(get("/leads/{id}/edit", id))
        .andExpect(status().isOk())
        .andExpect(view().name("spring/edit"))
        .andExpect(model().attributeExists("lead"))
        .andExpect(model().attribute("lead", existing));
  }

  @Test
  @DisplayName("Should return 404 when editing non-existing lead")
  void shouldReturn404WhenEditNonExistingLead() throws Exception {
    UUID id = UUID.randomUUID();

    when(leadService.findById(id)).thenReturn(Optional.empty());

    mockMvc.perform(get("/leads/{id}/edit", id))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @DisplayName("Should delete lead and redirect to list")
  void shouldDeleteLeadAndRedirect() throws Exception {
    UUID id = UUID.randomUUID();

    mockMvc.perform(post("/leads/{id}/delete", id))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/leads"));

    verify(leadService).deleteLead(id);
  }

  @Test
  @DisplayName("Should return 404 when deleting non-existing lead")
  void shouldReturn404WhenDeleteNonExistingLead() throws Exception {
    UUID id = UUID.randomUUID();

    doThrow(new NoSuchElementException("Lead not found: " + id))
        .when(leadService).deleteLead(id);

    mockMvc.perform(post("/leads/{id}/delete", id))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Should filter leads by email and status")
  void shouldFilterByEmailAndStatus() throws Exception {
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    List<Lead> filtered = List.of(
        new Lead(id1, "NEW0@mail.ru", "+7900123456", "Company", LeadStatus.NEW),
        new Lead(id2, "NEW1@mail.ru", "+7901", "Company", LeadStatus.NEW)
    );

    when(leadService.findLeads("NEW0@mail.ru", LeadStatus.NEW)).thenReturn(filtered);

    mockMvc.perform(get("/leads")
            .param("search", "NEW0@mail.ru")
            .param("status", "NEW"))
        .andExpectAll(
            status().isOk(),
            model().attributeExists("leads"),
            model().attribute("leads", filtered),
            model().attribute("search", "NEW0@mail.ru")
        );

    verify(leadService).findLeads("NEW0@mail.ru", LeadStatus.NEW);
  }

  @Test
  @DisplayName("Should filter leads by status")
  void shouldFilterByStatus() throws Exception {
    List<Lead> filtered = List.of(
        new Lead(UUID.randomUUID(), "NEW0@mail.ru", "+7900123456", "Company", LeadStatus.NEW),
        new Lead(UUID.randomUUID(), "NEW1@mail.ru", "+7901", "Company", LeadStatus.NEW),
        new Lead(UUID.randomUUID(), "QUALIFIED0@mail.ru", "+7901", "Company", LeadStatus.QUALIFIED),
        new Lead(UUID.randomUUID(), "CONVERTED0@mail.ru", "+7901", "Company", LeadStatus.CONVERTED)
    );

    when(leadService.findLeads("", LeadStatus.NEW)).thenReturn(filtered);

    mockMvc.perform(get("/leads")
            .param("search", "")
            .param("status", "NEW"))
        .andExpectAll(
            status().isOk(),
            model().attributeExists("leads"),
            model().attribute("leads", filtered),
            model().attribute("status", LeadStatus.NEW)
        );

    verify(leadService).findLeads("", LeadStatus.NEW);
  }

  @Test
  @DisplayName("Should return form with errors when email is blank")
  void shouldReturnFormWithErrorsWhenEmailBlank() throws Exception {
    mockMvc.perform(post("/leads")
            .param("email", "")
            .param("phone", "+7900123456")
            .param("company", "Corp")
            .param("status", "NEW"))
        .andExpect(status().isOk())
        .andExpect(view().name("leads/create"))
        .andExpect(model().attributeHasFieldErrors("lead", "email"));
  }

  @Test
  @DisplayName("Should return form with errors when email format is invalid")
  void shouldReturnFormWithErrorsWhenEmailInvalid() throws Exception {
    mockMvc.perform(post("/leads")
            .param("email", "not-an-email")
            .param("phone", "+7900123456")
            .param("company", "Corp")
            .param("status", "NEW"))
        .andExpect(status().isOk())
        .andExpect(view().name("leads/create"))
        .andExpect(model().attributeHasFieldErrors("lead", "email"));
  }

  @Test
  @DisplayName("Should redirect when all fields are valid")
  void shouldRedirectWhenAllFieldsAreValid() throws Exception {
    mockMvc.perform(post("/leads")
            .param("email", "valid@mail.ru")
            .param("phone", "+7900123456")
            .param("company", "Corp")
            .param("status", "NEW"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/leads"));

    verify(leadService).addLead(any());
  }

  @Test
  @DisplayName("Should return edit form with errors when update has blank field")
  void shouldReturnEditFormWithErrorsOnInvalidUpdate() throws Exception {
    UUID id = UUID.randomUUID();

    mockMvc.perform(post("/leads/{id}", id)
            .param("email", "")
            .param("phone", "+7900123456")
            .param("company", "Corp")
            .param("status", "NEW"))
        .andExpect(status().isOk())
        .andExpect(view().name("spring/edit"))
        .andExpect(model().attributeHasFieldErrors("lead", "email"));
  }

}
