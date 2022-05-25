package org.ical4j.validator.command;

import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.util.Calendars;
import net.fortuna.ical4j.validate.ValidationReport;
import net.fortuna.ical4j.validate.ValidationResult;
import picocli.CommandLine;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

@CommandLine.Command(name = "report", description = "Produce a validation report for input data")
public class ValidationReportCommand implements Runnable {

    @CommandLine.Option(names = {"-F", "--format"})
    private ValidationReport.Format reportFormat;

    @CommandLine.Option(names = {"-U", "--url"})
    private URL url;

    @Override
    public void run() throws RuntimeException {
        try {
            Calendar cal = Calendars.load(url);
            ValidationResult result = cal.validate();
            if (result.hasErrors()) {
                StringWriter out = new StringWriter();
                new ValidationReport(ValidationReport.Format.TEXT).output(result, out);
                System.out.print(out);
            } else {
                System.out.print("No errors.");
            }
        } catch (IOException | ParserException e) {
            throw new RuntimeException(e);
        }
    }
}
