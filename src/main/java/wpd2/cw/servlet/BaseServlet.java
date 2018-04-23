
package wpd2.cw.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.cw.util.mustache.MustacheRenderer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

class BaseServlet extends HttpServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(BaseServlet.class);

    static final String HTML_UTF_8 = "text/html; charset=UTF-8";
    //static final String PLAIN_TEXT_UTF_8 = "text/plain; charset=UTF-8";
    static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    static final List<Pattern> PROTECTED_PAGES = Arrays.asList(Pattern.compile("/private"), Pattern.compile("/messages"));
    static final String LOGIN_PAGE = "/login";

    private final MustacheRenderer mustache;

    BaseServlet() {
        mustache = new MustacheRenderer();
    }

    void issue(String mimeType, int returnCode, byte[] output, HttpServletResponse response) throws IOException {
        response.setContentType(mimeType);
        response.setStatus(returnCode);
        response.getOutputStream().write(output);
    }

    void showView(HttpServletResponse response, String templateName, Object model) throws IOException {
        String html = mustache.render(templateName, model);
        issue(HTML_UTF_8, HttpServletResponse.SC_OK, html.getBytes(CHARSET_UTF8), response);
    }

    String getString(HttpServletRequest request, String param, String deflt) {
        String value = request.getParameter(param);
        if (value == null) {
            return deflt;
        }
        return value;
    }

    boolean getBoolean(HttpServletRequest request, String param) {
        String value = request.getParameter(param);
        return value != null;
    }

    long getLong(HttpServletRequest request, String param) {
        String value = request.getParameter(param);
        return (value == null) ? 0 : Long.parseLong(value);
    }

    boolean authOK(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        String userName = UserFuncs.getCurrentUser(request);
        if (isProtected(uri) && "".equals(userName)) {
            UserFuncs.setLoginRedirect(request);
            response.sendRedirect(response.encodeRedirectURL(LOGIN_PAGE));
            return false;
        }
        return true;
    }

    private boolean isProtected(String page) {
        for (Pattern p : PROTECTED_PAGES) {
            if (p.matcher(page).matches()) {
                return true;
            }
        }
        return false;
    }

}
