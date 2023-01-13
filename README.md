# ical4j-validator

A simple validation service for iCalendar

## Examples

### Generate validation report

    ical4j-validator/bin/ical4j-validator report -F ./OZMovies.ics

Result:

```
- ERROR: VCALENDAR - If one is present, ALL others MUST NOT be present: VEVENT,VFREEBUSY,VTODO,VJOURNAL
```

