package wpd2.cw.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MenuServlet extends BaseServlet{

    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(PublicPageServlet.class);

    private static final String MENU_TEMPLATE = "menu.mustache";

    public MenuServlet() {}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authOK(request, response)) {
            return;
        }
        String userName = UserFuncs.getCurrentUser(request);
        showView(response, MENU_TEMPLATE, userName);
    }
}

