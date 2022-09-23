package org.ical4j.validator;

import org.ical4j.validator.command.JettyRunCommand;
import org.ical4j.validator.command.ValidationReportCommand;
import org.ical4j.validator.command.VersionProvider;
import picocli.CommandLine;

@CommandLine.Command(name = "validator", description = "iCal4j Validator",
        subcommands = {ValidationReportCommand.class, JettyRunCommand.class},
        mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
public class ICalendarValidatorMain {

    public static void main(String[] args) throws Exception {
        new CommandLine(new ICalendarValidatorMain()).execute(args);
    }
}
