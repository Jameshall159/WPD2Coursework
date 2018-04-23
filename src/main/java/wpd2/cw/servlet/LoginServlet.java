package wpd2.cw.servlet;

import wpd2.cw.dbdemo.db.H2Person;
import wpd2.cw.dbdemo.model.Person;
import wpd2.cw.util.mustache.MustacheRender;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;


/**
 * Servlet for view all persons in the DB
 */
public class LoginServlet extends BaseServlet {

    private final H2Person h2Person;
    private final MustacheRender mustache;

    public LoginServlet(H2Person h2Person) {
        mustache = new MustacheRender();
        this.h2Person = h2Person;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String html = mustache.render("login.mustache", new PersonsClass());

        // Set the content type and status code and render/write the response
        response.setContentType("text/html");
        response.setStatus(200);
        response.getOutputStream().write(html.getBytes(Charset.forName("utf-8")));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter(UserFuncs.USERNAME_PARAMETER);
        if (email != null && email.length() > 0) {
            UserFuncs.setCurrentUser(request, email);
            String targetURL = UserFuncs.getLoginRedirect(request);
            response.sendRedirect(response.encodeRedirectURL(targetURL));
        }


    }
    private class PersonsClass {
        List<Person> persons() {
            return h2Person.findPersons();
        }

    }
}
