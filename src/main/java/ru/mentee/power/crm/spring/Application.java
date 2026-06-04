package ru.mentee.power.crm.spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

// ! BCORE-15 не видит "вверх по дереву" т.к. корневая папка выше. Указал явно временно
@SpringBootApplication(scanBasePackages = "ru.mentee.power.crm")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  // Передаём лямбду в виде лидов в args main для заполнения таблицы лидов
  @Bean
  CommandLineRunner seedLeads(LeadService service) {
    return args -> {
      for (int i = 0; i < 5; i++) {
        service.addLead(
            "email" + i + "@mail.ru",
            "+7900" + i,
            "Company #" + i,
            LeadStatus.NEW
        );
      }
    };
  }

}
