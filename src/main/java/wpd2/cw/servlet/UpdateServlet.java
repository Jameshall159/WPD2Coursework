package wpd2.cw.servlet;

import com.google.common.base.Charsets;
//import com.sun.xml.internal.bind.v2.model.core.ID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.cw.dbdemo.db.H2User;
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
import java.util.Random;

@Singleton
public class UpdateServlet extends BaseServlet {

    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(addMilestoneServlet.class);
    java.util.Date d = new java.util.Date();
    private static final String UPDATE_TEMPLATE = "update.mustache";
    private static final String MESSAGE_PARAMETER = "message";
    private static final String DESCRIPTION_PARAMETER = "description";
    private static final String EXPECTED_PARAMETER = "expectedComplete";
    private static final String LINK_PARAMETER = "link";
    private static final String USER_PARAMETER = "user";
    private static final String METHOD_PARAMETER = "method";
    private static final String ID_PARAMETER = "id";


    private final IMessageDB db;
    private final H2User userDB;


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authOK(request, response)) {
            return;
        }
        String link = linkFromRequest(request);
        String userName = userFromRequest(request);
        String loggedInUser = UserFuncs.getCurrentUser(request);

        if (db.link(link) == null) {
            String err = "No such milestone: " + link;
            issue("text/plain", HttpServletResponse.SC_NOT_FOUND, err.getBytes(Charsets.UTF_8), response);
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("user", userName);
        map.put("matches", loggedInUser.equals(userName));

        List<Message> messages = db.link(link);
        if (messages.size() > 0) {
            map.put("messages", messages);
        }
        showView(response, UPDATE_TEMPLATE, map);

    }

    @Inject
    public UpdateServlet(IMessageDB db, H2User userDB) {
        this.db = db;
        this.userDB = userDB;
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

    public static String randomString(int length) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < length; i++) {
            b.append(base.charAt(random.nextInt(base.length())));
        }
        return b.toString();
    }

    String link5 = randomString(7);

    private static String base = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabsdefghijklmnopqrstuvwxyz";
    private static Random random = new Random();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authOK(request, response)) {
            return;
        }
        String method = getString(request, METHOD_PARAMETER, "post");
        if ("delete".equals(method)) {
            doDelete(request, response);
        } else {
            String user = request.getParameter(USER_PARAMETER);
            long id = getLong(request, ID_PARAMETER);
            String message = request.getParameter(MESSAGE_PARAMETER);
            String description = request.getParameter(DESCRIPTION_PARAMETER);
            String expectedComplete = request.getParameter(EXPECTED_PARAMETER);
            int actual = 0;
            String link = request.getParameter(LINK_PARAMETER);
            String userName = userFromRequest(request);
//        Message m = new Message();
//        m.setid(id);
//        e.setName(name);
//        e.setPassword(password);
//        e.setEmail(email);
//        e.setCountry(country);
            if (!userDB.isRegistered(userName)) {
                String err = "Milestone URL invalid " + userName;
                issue("text/plain", HttpServletResponse.SC_NOT_FOUND, err.getBytes(Charsets.UTF_8), response);
                return;
            }
            db.update(message, description, user, expectedComplete, actual, link);
            response.sendRedirect(response.encodeRedirectURL(request.getRequestURI()));
        }
    }
}





