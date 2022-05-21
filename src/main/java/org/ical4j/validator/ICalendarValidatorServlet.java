package org.ical4j.validator;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.util.Calendars;
import net.fortuna.ical4j.validate.ValidationReport;
import net.fortuna.ical4j.validate.ValidationResult;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

@Component(
        service = {HttpServlet.class, Servlet.class},
        property = {"service.description=iCalendar Validator Servlet"}
)
@Designate(ocd = ICalendarValidatorServletConfiguration.class, factory = true)
public class ICalendarValidatorServlet extends HttpServlet {

    private byte[] form;

    @Override
    public void init() throws ServletException {
        super.init();
        try (InputStream data = getClass().getResourceAsStream("/form.html")) {
            this.form = Objects.requireNonNull(data).readAllBytes();
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getParameter("url");
        if (url != null) {
            try {
                Calendar cal = Calendars.load(new URL(url));
                ValidationResult result = cal.validate();
                if (result.hasErrors()) {
                    resp.getWriter().println("<html><body>");
                    new ValidationReport(ValidationReport.Format.HTML).output(result, resp.getWriter());
                    resp.getWriter().println("</body></html>");
                } else {
                    resp.getWriter().println("No errors.");
                }
            } catch (ParserException e) {
                throw new ServletException(e);
            }
        } else {
            resp.getOutputStream().write(form);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
