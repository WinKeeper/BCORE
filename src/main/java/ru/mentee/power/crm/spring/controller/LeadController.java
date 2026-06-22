package ru.mentee.power.crm.spring.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;

@Controller
public class LeadController {

  private final LeadService leadService;

  public LeadController(LeadService leadService) {
    this.leadService = leadService;
  }

  @GetMapping("/")
  @ResponseBody
  public String home() {
    return "Spring Boot CRM is running! Bean created: " + leadService.findAll().size() + " leads.";
  }

  @GetMapping("/leads/new")
  public String showCreateForm(Model model) {
    Lead empty = new Lead(null, "", "", "", LeadStatus.NEW);
    model.addAttribute("lead", empty);
    model.addAttribute("errors", new BeanPropertyBindingResult(empty, "lead"));
    return "leads/create";
  }


  @PostMapping("/leads")
  public String createLead(@Valid @ModelAttribute Lead lead, BindingResult result, Model model) {
    if (result.hasErrors()) {
      model.addAttribute("errors", result);
      return "leads/create";
    }
    leadService.addLead(lead);
    return "redirect:/leads";
  }

  @GetMapping("/leads")
  public String showLeads(@RequestParam(required = false) String search,
                          @RequestParam(required = false) LeadStatus status,
                          Model model) {

    List<Lead> leads = leadService.findLeads(search, status);

    model.addAttribute("leads", leads);
    model.addAttribute("status", status);
    model.addAttribute("search", search != null ? search : "");

    return "leads/list";
  }

  @GetMapping("/leads/{id}/edit")
  public String showEditForm(@PathVariable UUID id, Model model) {
    Lead lead = leadService.findById(id)
        .orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found: " + id));
    model.addAttribute("lead", lead);
    model.addAttribute("errors", new BeanPropertyBindingResult(lead, "lead"));
    return "spring/edit";
  }

  @PostMapping("/leads/{id}")
  public String updateLead(@PathVariable UUID id, @Valid @ModelAttribute Lead lead,
                           BindingResult result, Model model) {
    if (result.hasErrors()) {
      model.addAttribute("errors", result);
      return "spring/edit";
    }
    leadService.updateLead(id, lead);
    return ("redirect:/leads");
  }

  @PostMapping("/leads/{id}/delete")
  public String deleteLead(@PathVariable UUID id) {
    try {
      leadService.deleteLead(id);
    } catch (NoSuchElementException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found: " + id);
    }
    return ("redirect:/leads");
  }
}