
package wpd2.cw;

import wpd2.cw.dbdemo.db.H2Person;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.cw.servlet.*;
import wpd2.cw.servlet.PersonServlet;
import wpd2.cw.servlet.ListPersonsServlet;
import wpd2.cw.servlet.RegisterServlet;


import java.util.Locale;


public class Runner {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(Runner.class);

    private static final int PORT = 9000;

    private final H2Person h2Person;

    public Runner() {
        h2Person = new H2Person();
    }

    public void start() throws Exception {
        Server server = new Server(PORT);

        ServletContextHandler handler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        handler.setInitParameter("org.eclipse.jetty.servlet.Default." + "resourceBase", "src/main/resources/webapp");

        handler.addServlet(new ServletHolder(new PersonServlet(h2Person)), "/index");
        handler.addServlet(new ServletHolder(new PersonServlet(h2Person)), "/add"); // we post to here


        //Maps ListPersonsServlet to process requests to "localhost:9000/view"
        handler.addServlet(new ServletHolder(new ListPersonsServlet(h2Person)), "/view");

        //Login
        handler.addServlet(new ServletHolder(new LoginServlet(h2Person)), "/login");

        //register
        handler.addServlet(new ServletHolder(new RegisterServlet(h2Person)), "/register");


        //home
        handler.addServlet(new ServletHolder(new HomeServlet(h2Person)), "/home");

        DefaultServlet ds = new DefaultServlet();
        handler.addServlet(new ServletHolder(ds), "/");


        server.start();
        LOG.info("Server started, will run until terminated");
        server.join();
    }

    public static void main(String[] args) {
        try {
            LOG.info("starting dbdemo");
            Locale.setDefault(Locale.UK);
            new Runner().start();
        } catch (Exception e) {
            LOG.error("Unexpected error running dbdemo: " + e.getMessage());
        }
    }
}
