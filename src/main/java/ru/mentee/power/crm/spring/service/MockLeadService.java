package ru.mentee.power.crm.spring.service;

import java.util.List;
import java.util.UUID;

import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;

public class MockLeadService extends LeadService {
  private final List<Lead> mockLeads;

  public MockLeadService() {
    super(null);
    this.mockLeads = List.of(
        new Lead(UUID.randomUUID(), "mail0@mail.com", "123123", "BigTechCorp", LeadStatus.NEW),
        new Lead(UUID.randomUUID(), "mail1@mail.com", "123123", "BigTechCorp", LeadStatus.NEW)
    );
  }

  @Override
  public List<Lead> findAll() {
    return mockLeads;
  }

}
