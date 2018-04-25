package wpd2.cw.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.cw.dbdemo.db.IMessageDB;
import wpd2.cw.dbdemo.model.Message;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class MessagesServlet extends BaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(MessagesServlet.class);

    private static final String MESSAGES_TEMPLATE = "messages.mustache";
    private static final String MESSAGE_PARAMETER = "message";
    private static final String DESCRIPTION_PARAMETER = "description";
    private static final String EXPECTED_PARAMETER = "expectedComplete";
    private static final String LINK_PARAMETER = "link";

    private final IMessageDB db;

    @Inject
    public MessagesServlet(IMessageDB db) {
        this.db = db;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authOK(request, response)) {
            return;
        }
        List<Message> messages = db.list();
        Map<String,Object> map = new HashMap<>();
        String userName = UserFuncs.getCurrentUser(request);
        map.put("user", userName);
        if (messages.size() > 0) {
            map.put("messages", messages);
        }
        showView(response, MESSAGES_TEMPLATE, map);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authOK(request, response)) {
            return;
        }
        String message = request.getParameter(MESSAGE_PARAMETER);
        String description = request.getParameter(DESCRIPTION_PARAMETER);
        String expectedComplete = request.getParameter(EXPECTED_PARAMETER);
        int actual = 0;
        String link = request.getParameter(LINK_PARAMETER);
        String user = UserFuncs.getCurrentUser(request);
        db.add(message, description, user, expectedComplete, actual, link);
        response.sendRedirect(response.encodeRedirectURL(request.getRequestURI()));
    }

}
