package wpd2.cw.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class PrivatePageServlet extends BaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(PrivatePageServlet.class);

    private static final String PRIVATE_PAGE_TEMPLATE = "private.mustache";

    public PrivatePageServlet() {}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authOK(request, response)) {
            return;
        }
        String userName = UserFuncs.getCurrentUser(request);
        showView(response, PRIVATE_PAGE_TEMPLATE, userName);
    }

}
