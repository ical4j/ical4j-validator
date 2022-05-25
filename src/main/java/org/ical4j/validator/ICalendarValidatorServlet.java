package org.ical4j.validator;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.data.UnfoldingReader;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.validate.ValidationReport;
import net.fortuna.ical4j.validate.ValidationResult;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

@Component(
        service = Servlet.class,
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
                CalendarBuilder builder = new CalendarBuilder();
                UnfoldingReader reader = new UnfoldingReader(new InputStreamReader(new URL(url).openStream()), true);
//                Calendar cal = Calendars.load(new URL(url));
                Calendar cal = builder.build(reader);
                ValidationResult result = cal.validate();
                resp.getWriter().println("<html lang=\"en\"><body>");
                if (result.hasErrors()) {
                    new ValidationReport(ValidationReport.Format.HTML).output(result, resp.getWriter());
                } else {
                    resp.getWriter().println("<p>No errors.</p>");
                }
                resp.getWriter().println("<p><a href=\"" + getServletContext().getContextPath() + "\">Home</a></p>");
                resp.getWriter().println("</body></html>");
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
