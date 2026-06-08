package ru.mentee.power.crm.spring.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

class LeadControllerTest {

  private MockMvc mockMvc;
  private LeadService leadService;

  @BeforeEach
  void setUp() {
    leadService = mock(LeadService.class);
    mockMvc = MockMvcBuilders.standaloneSetup(new LeadController(leadService)).build();
  }

  @Test
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
  void shouldHandleEmptyList() throws Exception {
    when(leadService.findAll()).thenReturn(List.of());

    mockMvc.perform(get("/leads"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("leads", hasSize(0)));
  }
}
