package ru.mentee.power.crm.web;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.InMemoryLeadRepository;
import ru.mentee.power.crm.service.LeadService;
import ru.mentee.power.crm.servlet.LeadListServlet;

public class Main {
  static void main() throws Exception {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();
    LeadService leadService = new LeadService(repository);

    for (int i = 0; i < 5; i++) {
      leadService.addLead("Email" + i + "@mail.ru", "12345" + i, "Bcore-13 #" + i, LeadStatus.NEW);
    }

    leadService.addLead("<script>alert('XSS')</script>", "12345", "test", LeadStatus.NEW);

    System.out.println("Lead count: " + leadService.findAll().size());

    Tomcat tomcat = new Tomcat();
    tomcat.setPort(8080);

    Context context = tomcat.addContext("", new File(".").getAbsolutePath());
    context.getServletContext().setAttribute("leadService", leadService);

    tomcat.addServlet(context, "LeadListServlet", new LeadListServlet());
    context.addServletMappingDecoded("/leads", "LeadListServlet");

    tomcat.start();

    System.out.println("Tomcat started on port: " + tomcat.getConnector().getLocalPort());
    System.out.println("Open http://" + tomcat.getHost().getName() + ":8080/leads in browser");

    tomcat.getServer().await();

  }
}
