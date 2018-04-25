package wpd2.cw.dbdemo.guice;


import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.cw.dbdemo.db.ConnectionSupplier;
import wpd2.cw.dbdemo.db.H2User;
import wpd2.cw.dbdemo.db.IMessageDB;
import wpd2.cw.dbdemo.db.MemMessages;
import wpd2.cw.servlet.LogoutServlet;
import wpd2.cw.servlet.PrivatePageServlet;
import wpd2.cw.servlet.PublicPageServlet;
import wpd2.cw.servlet.HomeServlet;
import wpd2.cw.servlet.addMilestoneServlet;


class BindingModule extends AbstractModule {
    @SuppressWarnings({"unused"})
    static final Logger LOG = LoggerFactory.getLogger(BindingModule.class);

    BindingModule() {}

    @Override
    protected void configure() {
        // rather than bind the servlets you can add an <code>@Singleton</code> annotation
        // just before the class declaration (see <code>LoginServlet</code>).
        bind(PrivatePageServlet.class).in(Scopes.SINGLETON);  // only one instance in the whole app
        bind(PublicPageServlet.class).in(Scopes.SINGLETON);
        bind(LogoutServlet.class).in(Scopes.SINGLETON);
        bind(HomeServlet.class).in(Scopes.SINGLETON);  // only one instance in the whole app
        bind(addMilestoneServlet.class).in(Scopes.SINGLETON);  // only one instance in the whole app


        bind(H2User.class).toInstance(new H2User(new ConnectionSupplier(ConnectionSupplier.MEMORY)));
        bind(IMessageDB.class).toInstance(new MemMessages());
    }
}
