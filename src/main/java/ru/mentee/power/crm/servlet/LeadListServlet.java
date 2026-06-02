package ru.mentee.power.crm.servlet;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.output.WriterOutput;
import gg.jte.resolve.DirectoryCodeResolver;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.service.LeadService;

@WebServlet("/leads")
public class LeadListServlet extends HttpServlet {

  private TemplateEngine templateEngine;

  @Override
  public void init() throws ServletException {
    Path templatePath = Path.of("src/main/jte");
    // Сканер шаблонов: читает .jte файлы из папки при старте
    DirectoryCodeResolver codeResolver = new DirectoryCodeResolver(templatePath);
    // Создать движок: указать откуда брать шаблоны и тип контента (Html)
    this.templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
  }

  void setTemplateEngine(TemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    LeadService service = (LeadService) getServletContext().getAttribute("leadService");
    if (service == null) {
      throw new IllegalStateException("LeadService not found in ServletContext");
    }
    List<Lead> leads = service.findAll();

    Map<String, Object> model = new HashMap<>();
    model.put("leads", leads);

    response.setContentType("text/html; charset=UTF-8");
    response.setStatus(200);

    templateEngine.render("leads/list.jte", model, new WriterOutput(response.getWriter()));

    System.out.println("Response sent successfully");
  }

}
