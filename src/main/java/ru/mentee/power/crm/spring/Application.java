package ru.mentee.power.crm.spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

// ! BCORE-15 не видит "вверх по дереву" т.к. корневая папка выше. Указал явно временно
@SpringBootApplication(scanBasePackages = {
    "ru.mentee.power.crm.spring",
    "ru.mentee.power.crm.service",
    "ru.mentee.power.crm.repository",
    "ru.mentee.power.crm.model"
})
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  CommandLineRunner seedLeads(LeadService service) {
    return args -> {
      for (int i = 0; i < 3; i++) {
        service.addLead(
            "NEW" + i + "@mail.ru",
            "+7900" + i,
            "Company #" + i,
            LeadStatus.NEW
        );
      }
      for (int j = 0; j < 5; j++) {
        service.addLead(
            "CONTACTED" + j + "@mail.ru",
            "+7900" + j,
            "Company #" + j,
            LeadStatus.CONTACTED
        );
      }
      for (int k = 0; k < 2; k++) {
        service.addLead(
            "QUALIFIED" + k + "@mail.ru",
            "+7900" + k,
            "Company #" + k,
            LeadStatus.QUALIFIED
        );
      }
    };
  }
}

