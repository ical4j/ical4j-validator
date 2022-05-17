package org.ical4j.validator;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "iCalendar Validator Servlet", description = "Servlet Configuration for an iCalendar validator")
@interface ICalendarValidatorServletConfiguration {

    @AttributeDefinition(name = "alias", description = "Servlet alias")
    String alias() default "/validator";
}
