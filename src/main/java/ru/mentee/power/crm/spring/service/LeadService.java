package ru.mentee.power.crm.spring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

@Service
public class LeadService {

  // Добавил логер
  private static final Logger log = LoggerFactory.getLogger(LeadService.class);

  private final LeadRepository<Lead> repository;

  public LeadService(LeadRepository<Lead> repository) {
    this.repository = repository;
    log.info("LeadService constructor called");
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

  public Lead addLead(Lead lead) {
    return addLead(lead.email(), lead.phone(), lead.company(), lead.status());
  }

  public Lead updateLead(UUID id, Lead updatedLead) {
    Lead existing =
        findById(id).orElseThrow(() -> new NoSuchElementException("Lead not found: " + id));

    if (!existing.email().equals(updatedLead.email())) {
      Optional<Lead> byEmail = findByEmail(updatedLead.email());
      if (byEmail.isPresent()) {
        throw new IllegalStateException("Email us already taken: " + updatedLead.email());
      }
    }

    repository.delete(id);
    Lead saved = new Lead(id, updatedLead.email(), updatedLead.phone(), updatedLead.company(),
        updatedLead.status());
    repository.save(saved);
    return saved;

  }

  public void deleteLead(UUID id) {
    findById(id).orElseThrow(() -> new NoSuchElementException("Lead not found: " + id));
    repository.delete(id);
    log.info("Lead with id: " + id + "successfully deleted");
  }

  public List<Lead> findAll() {
    return new ArrayList<>(repository.findAll());
  }

  public List<Lead> findByStatus(LeadStatus status) {
    return repository.findAll().stream()
        .filter(lead -> lead.status().equals(status))
        .collect(Collectors.toList());
  }

  public Optional<Lead> findById(UUID id) {
    return repository.findById(id);
  }

  public Optional<Lead> findByEmail(String email) {
    return repository.findByEmail(email);
  }

  public List<Lead> findLeads(String search, LeadStatus status) {
    Stream<Lead> stream = repository.findAll().stream();
    if (search != null && !search.isBlank()) {
      String lower = search.toLowerCase();
      stream = stream.
          filter(lead ->
              lead.email().toLowerCase().contains(lower) ||
                  lead.company().toLowerCase().contains(lower));
    }

    if (status != null) {
      stream = stream.filter(lead -> lead.status() == status);
    }

    return stream.collect(Collectors.toList());
  }

  @PostConstruct
  void init() {
    log.info("LeadService @PostConstruct init() called - Bean lifecycle phase");
  }
}
