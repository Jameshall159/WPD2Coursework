package wpd2.cw.servlet;

import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.cw.dbdemo.db.H2Messages;
import wpd2.cw.dbdemo.db.H2User;
import wpd2.cw.dbdemo.db.IMessageDB;
import wpd2.cw.dbdemo.db.MemMessages;
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
public class SharedServlet extends BaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(UserMessagesServlet.class);

    private static final String LINK_MESSAGES_TEMPLATE = "sharedmilestone.mustache";
    private static final String MESSAGE_PARAMETER = "message";
    private static final String DESCRIPTION_PARAMETER = "description";
    private static final String EXPECTED_PARAMETER = "expectedComplete";
    private static final String LINK_PARAMETER = "link";
    private static final String METHOD_PARAMETER = "method";
    private static final String ID_PARAMETER = "msgId";

    private final IMessageDB db;
    private final H2User userDB;


    @Inject
    public SharedServlet(IMessageDB db, H2User userDB) {
        this.db = db;
        this.userDB = userDB;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authOK(request, response)) {
            return;
        }
        String link = linkFromRequest(request);
        String loggedInUser = UserFuncs.getCurrentUser(request);
        if (db.link(link) == null) {
            String err = "No such milestone: " + link;
            issue("text/plain", HttpServletResponse.SC_NOT_FOUND, err.getBytes(Charsets.UTF_8), response);
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("user", link);
        map.put("matches", loggedInUser.equals(loggedInUser));

        List<Message> messages = db.link(link);
        if (messages.size() > 0) {
            map.put("messages", messages);
        }
        showView(response, LINK_MESSAGES_TEMPLATE, map);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authOK(request, response)) {
            return;
        }
        String method = getString(request, METHOD_PARAMETER, "post");
        if ("delete".equals(method)) {
            doDelete(request, response);
        } else {
            String message = request.getParameter(MESSAGE_PARAMETER);
            String description = request.getParameter(DESCRIPTION_PARAMETER);
            String expectedComplete = request.getParameter(EXPECTED_PARAMETER);
            int actual = 0;
            String link = request.getParameter(LINK_PARAMETER);
            String userName = userFromRequest(request);
            if (!userDB.isRegistered(userName)) {
                String err = "Milestone URL invalid " + userName;
                issue("text/plain", HttpServletResponse.SC_NOT_FOUND, err.getBytes(Charsets.UTF_8), response);
                return;
            }
            db.add(message, description, userName, expectedComplete, actual, link);
            response.sendRedirect(response.encodeRedirectURL(request.getRequestURI()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authOK(request, response)) {
            return;
        }
        String loggedInUser = UserFuncs.getCurrentUser(request);
        long id = getLong(request, ID_PARAMETER);
        Message m = db.get(id);
        if (m != null && loggedInUser.equals(m.getUser())) {
            db.delete(id);
        }
        response.sendRedirect(response.encodeRedirectURL(request.getRequestURI()));
    }

    static String userFromRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String[] sub = uri.split("/");
        if (sub.length == 3) {
            return sub[2];
        }
        return "";
    }

    static String linkFromRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String[] sub = uri.split("/");
        if (sub.length == 3) {
            return sub[2];
        }
        return "";
    }

}
