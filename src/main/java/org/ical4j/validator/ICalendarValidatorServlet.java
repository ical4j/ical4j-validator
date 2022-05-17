package org.ical4j.validator;

import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.util.Calendars;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

@Component(
        service = {javax.servlet.http.HttpServlet.class, javax.servlet.Servlet.class},
        property = {"service.description=iCalendar Validator Servlet"}
)
@Designate(ocd = ICalendarValidatorServletConfiguration.class, factory = true)
public class ICalendarValidatorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getParameter("url");
        if (url != null) {
            try {
                Calendar cal = Calendars.load(new URL(url));
                resp.getWriter().print(cal.validate().toString());
            } catch (ParserException e) {
                throw new ServletException(e);
            }
        }
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
