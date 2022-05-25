package org.ical4j.validator;

import org.ical4j.validator.command.JettyRunCommand;
import org.ical4j.validator.command.ValidationReportCommand;
import picocli.CommandLine;

@CommandLine.Command(name = "validator", description = "iCal4j Validator",
        subcommands = {ValidationReportCommand.class, JettyRunCommand.class})
public class ICalendarValidatorMain implements Runnable {

    @Override
    public void run() {
        System.out.println("iCal4j Validator. Usage: validator <subcommand> [options]");
    }

    public static void main(String[] args) throws Exception {
        new CommandLine(new ICalendarValidatorMain()).execute(args);
    }
}
