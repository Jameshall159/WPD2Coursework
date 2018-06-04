package wpd2.cw;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebRunner {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(WebRunner.class);

    public WebRunner() {

    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        String rootPath = WebRunner.class.getClassLoader().getResource(".").toString();
        WebAppContext webapp = new WebAppContext(rootPath + "webapp", "");
        server.setHandler(webapp);

        server.start();
        server.join();
    }
}
