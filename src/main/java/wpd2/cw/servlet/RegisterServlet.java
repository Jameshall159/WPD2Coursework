package wpd2.cw.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.cw.dbdemo.db.H2User;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class RegisterServlet extends BaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(LoginServlet.class);

    private static final String REGISTER_TEMPLATE = "register.mustache";
    private static final String EMPTY_FIELD_ERR = "Neither the user name or password can be empty";
    private static final String BAD_PASSWORD_ERROR = "The password is incorrect";
    private static final String ERR_PARAM = "err";
    private static final String USER_NAME_PARAM = "userName";

    private H2User h2User;

    @Inject
    public RegisterServlet(H2User h2User) {
        this.h2User = h2User;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = UserFuncs.getCurrentUser(request);

        Map<String, String> params = new HashMap<>();
        params.put(USER_NAME_PARAM, userName);

        showView(response, REGISTER_TEMPLATE, params);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = getString(request, UserFuncs.USERNAME_PARAMETER, "");
        String password = getString(request, UserFuncs.USERPASS_PARAMETER, "");

        Map<String, String> params = new HashMap<>();
        params.put(USER_NAME_PARAM, name);

        if (name.length() == 0 || password.length() == 0) {
            // the user hasn't filled the form in properly, set errors are proceed
            params.put(ERR_PARAM, EMPTY_FIELD_ERR);
        } else if (!checkUserNameAndPassword(name, password)) {
            // password is incorrect for user name
            params.put(ERR_PARAM, BAD_PASSWORD_ERROR);
        }
        if (params.containsKey(ERR_PARAM)) {
            showView(response, REGISTER_TEMPLATE, params);
        } else {
            UserFuncs.setCurrentUser(request, name);
            String targetURL = UserFuncs.getLoginRedirect(request);
            response.sendRedirect(response.encodeRedirectURL(targetURL));
        }

    }

    private boolean checkUserNameAndPassword(String userName, String password) {
        if (h2User.register(userName, password)) {
            return true;
        }
        return false;
    }
}