
package wpd2.cw.dbdemo.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoginServeContext extends GuiceServletContextListener {
    @SuppressWarnings({"unused"})
    static final Logger LOG = LoggerFactory.getLogger(LoginServeContext.class);

    public LoginServeContext() {
        super();
    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
                // order is essential here, as the user filter must come befoe auth and so on
                new BindingModule(),
                new RouteModule());
    }
}
