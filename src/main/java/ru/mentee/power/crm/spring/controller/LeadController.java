package ru.mentee.power.crm.spring.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;

@Controller
public class LeadController {

  private final LeadService leadService;

  public LeadController(LeadService leadService) {
    this.leadService = leadService;
  }

  @GetMapping("/leads/new")
  public String showCreateForm(Model model) {
    model.addAttribute("lead", new Lead(null, "", "",
        "",
        LeadStatus.NEW, null));
    return "leads/create";
  }

  @PostMapping("/leads")
  public String createLead(@ModelAttribute Lead lead) {
    leadService.addLead(lead);
    return ("redirect:/leads");
  }

  @GetMapping("/leads")
  public String showLeads(@RequestParam(required = false) LeadStatus status,
                          @RequestParam(required = false) String sortBy,
                          @RequestParam(required = false, defaultValue = "asc") String sortDir,
                          Model model) {
    List<Lead> leads = (status == null)
        ? leadService.findAll()
        : leadService.findByStatus(status);

    if ("updatedAt".equals(sortBy)) {
      if ("desc".equals(sortDir)) {
        leads.sort((a, b) -> b.updatedAt().compareTo(a.updatedAt()));
      } else {
        leads.sort((a, b) -> a.updatedAt().compareTo(b.updatedAt()));
      }
    }

    model.addAttribute("leads", leads);
    model.addAttribute("currentFilter", status);
    model.addAttribute("sortBy", sortBy);
    model.addAttribute("sortDir", sortDir);

    return "leads/list";
  }

}