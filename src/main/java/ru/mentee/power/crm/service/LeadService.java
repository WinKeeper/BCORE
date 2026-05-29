package ru.mentee.power.crm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

public class LeadService {
  // TODO - потом удалить комментарии
  //  «в этом классе будет поле по имени repository,
  // и в него можно положить ссылку на любой объект,
  //  который реализует LeadRepository<Lead>»
  private final LeadRepository<Lead> repository;

  // DI через конструктор!!! — не создаём repository внутри!
  public LeadService(LeadRepository<Lead> repository) {
    // Передаём ссылку на repository во внутренний тип repository
    this.repository = repository;
  }

  public Lead addLead(String email, String phone, String company, LeadStatus status) {
    Optional<Lead> existing = repository.findByEmail(email);
    if (existing.isPresent()) {
      throw new IllegalStateException("Lead with email already exists: " + email);
    }

    Lead lead = new Lead(UUID.randomUUID(), email, phone, company, status);
    repository.save(lead);
    return lead;
  }

  public List<Lead> findAll() {
    return new ArrayList<>(repository.findAll());
  }

  public Optional<Lead> findById(UUID id) {
    return repository.findById(id);
  }

  public Optional<Lead> findByEmail(String email) {
    return repository.findByEmail(email);
  }
}
