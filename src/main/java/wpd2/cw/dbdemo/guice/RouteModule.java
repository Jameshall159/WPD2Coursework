package wpd2.cw.dbdemo.guice;

import com.google.inject.servlet.ServletModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.cw.servlet.*;

class RouteModule extends ServletModule {
    @SuppressWarnings({"unused"})
    static final Logger LOG = LoggerFactory.getLogger(RouteModule.class.getName());



    RouteModule() {}

    @Override
    protected void configureServlets() {
        serve("/login").with(LoginServlet.class);
        serve("/logout").with(LogoutServlet.class);
        serve("/private").with(PrivatePageServlet.class);
        serve("/public").with(PublicPageServlet.class);
        serve("/register").with(RegisterServlet.class);
        serve("/home").with(HomeServlet.class);
        serve("/addMilestone").with(addMilestoneServlet.class);


        serve("/messages").with(MessagesServlet.class);
        serve("/messages/*").with(UserMessagesServlet.class);
    }
}
