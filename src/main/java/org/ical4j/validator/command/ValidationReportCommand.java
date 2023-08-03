package org.ical4j.validator.command;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.util.Calendars;
import net.fortuna.ical4j.validate.ValidationReport;
import net.fortuna.ical4j.validate.ValidationResult;
import picocli.CommandLine;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

@CommandLine.Command(name = "report", description = "Produce a validation report for input data",
        subcommands = {CommandLine.HelpCommand.class})
public class ValidationReportCommand implements Runnable {

    @CommandLine.Option(names = {"-X", "--format"}, defaultValue = "TEXT")
    private ValidationReport.Format reportFormat;

    @CommandLine.ArgGroup(exclusive = true, multiplicity = "1")
    private Input input;

    static class Input {
        @CommandLine.Option(names = {"-U", "--url"}, required = true)
        private URL url;

        @CommandLine.Option(names = {"-F", "--file"}, required = true)
        private String filename;

        @CommandLine.Option(names = {"--stdin"}, required = true)
        private boolean stdin;
    }

    @Override
    public void run() throws RuntimeException {
        try {
            Calendar cal = null;
            if (input.filename != null) {
                cal = Calendars.load(input.filename);
            } else if (input.url != null) {
                cal = Calendars.load(input.url);
            } else if (input.stdin) {
                final CalendarBuilder builder = new CalendarBuilder();
                cal = builder.build(System.in);
            }
            ValidationResult result = cal.validate();
            if (result.hasErrors()) {
                StringWriter out = new StringWriter();
                new ValidationReport(reportFormat).output(result, out);
                System.out.print(out);
            } else {
                System.out.print("No errors.");
            }
        } catch (IOException | ParserException e) {
            throw new RuntimeException(e);
        }
    }
}
