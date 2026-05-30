package ru.mentee.power.crm.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.service.LeadService;

@WebServlet("/leads")
public class LeadListServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // TODO: Получить LeadService из ServletContext через getAttribute("$$$")
    // Hint: используйте getServletContext().getAttribute("$$$") и приведите к LeadService
    ServletContext context = getServletContext();
    LeadService service = (LeadService) context.getAttribute("leadService");

    // TODO: Вызвать leadService.findAll() и сохранить результат в локальной переменной List<Lead> leads
    List<Lead> leads = service.findAll();

    // TODO: Установить Content-Type через response.setContentType("$$$$")
    response.setContentType("text/html; charset=UTF-8");
    response.setStatus(200);

    // TODO: Получить PrintWriter через response.getWriter()
    PrintWriter writer = response.getWriter();

    // TODO: Сгенерировать HTML таблицу через writer.println() (см. Шаг 3)
    writer.println("<!DOCTYPE html>");
    writer.println("<html>");
    writer.println("<head><title>CRM - Lead List</title></head>");
    writer.println("<body>");
    writer.println("<h1>Lead List</h1>");
    writer.println("<table border='1'>");
    writer.println("<thead>");
    writer.println("<tr>");
    writer.println("<th>Email</th>");
    writer.println("<th>Company</th>");
    writer.println("<th>Status</th>");
    writer.println("</tr>");
    writer.println("</thead>");
    writer.println("<tbody>");

    for (Lead lead : leads) {
      writer.println("<tr>");
      writer.println("<td>" + lead.email() + "</td>");
      writer.println("<td>" + lead.company() + "</td>");
      writer.println("<td>" + lead.status() + "</td>");
      writer.println("</tr>");
    }

    writer.println("</tbody>");
    writer.println("</table>");
    writer.println("</body>");
    writer.println("</html>");

    System.out.println("Response sent successfully");
  }
}
