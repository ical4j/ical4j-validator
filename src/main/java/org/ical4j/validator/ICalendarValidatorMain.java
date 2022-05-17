package org.ical4j.validator;

import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.util.Calendars;
import net.fortuna.ical4j.validate.ValidationReport;
import net.fortuna.ical4j.validate.ValidationResult;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URL;

public class ICalendarValidatorMain {

    public static void main(String[] args) throws IOException, ParserException {
        Calendar cal = Calendars.load(new URL("https://www.vic.gov.au/sites/default/files/2021-09/Victorian-public-holiday-dates.ics"));
        ValidationResult result = cal.validate();
        if (result.hasErrors()) {
            StringWriter out = new StringWriter();
            new ValidationReport(ValidationReport.Format.TEXT).output(result, out);
            System.out.print(out);
        } else {
            System.out.print("No errors.");
        }
    }
}
