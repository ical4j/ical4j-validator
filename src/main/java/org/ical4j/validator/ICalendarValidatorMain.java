package org.ical4j.validator;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.util.Calendars;
import net.fortuna.ical4j.validate.ValidationReport;
import net.fortuna.ical4j.validate.ValidationResult;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.net.URL;

public class ICalendarValidatorMain {

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
//        Calendar cal = Calendars.load(new URL("https://www.vic.gov.au/sites/default/files/2021-09/Victorian-public-holiday-dates.ics"));
            Calendar cal = Calendars.load(new URL(args[0]));
            ValidationResult result = cal.validate();
            if (result.hasErrors()) {
                StringWriter out = new StringWriter();
                new ValidationReport(ValidationReport.Format.TEXT).output(result, out);
                System.out.print(out);
            } else {
                System.out.print("No errors.");
            }
        } else {
//            byte[] form = Files.readAllBytes(Path.of("src/main/resources/form.html"));
//            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
//            server.createContext("/", new HttpHandler() {
//                @Override
//                public void handle(HttpExchange exchange) throws IOException {
//                    String method = exchange.getRequestMethod();
//                    exchange.sendResponseHeaders(200, form.length);
//                    OutputStream os = exchange.getResponseBody();
//                    os.write(form);
//                    os.close();
//                }
//            });
//            server.setExecutor(null); // creates a default executor
//            server.start();

            Server server = new Server(8000);
            Connector connector = new ServerConnector(server);
            server.addConnector(connector);

            // Create a ServletContextHandler with contextPath.
            ServletContextHandler context = new ServletContextHandler();
            context.setContextPath("/validator");

            // Add the Servlet implementing the cart functionality to the context.
            ServletHolder servletHolder = context.addServlet(ICalendarValidatorServlet.class, "/");

            // Link the context to the server.
            server.setHandler(context);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("Shutting down the application...");
                    server.stop();
                    System.out.println("Done, exit.");
                } catch (Exception e) {
                    LoggerFactory.getLogger(ICalendarValidatorMain.class.getName()).error("Unexpected error", e);
                }
            }));

            server.start();
        }
    }
}
