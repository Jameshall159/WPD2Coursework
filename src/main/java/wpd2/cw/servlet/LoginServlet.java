package wpd2.cw.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import wpd2.cw.util.mustache.MustacheRender;
import wpd2.cw.dbdemo.db.H2Person;
import wpd2.cw.dbdemo.model.Person;
import java.util.List;


/**
 * Servlet for view all persons in the DB
 */
public class LoginServlet extends HttpServlet {

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


    private class PersonsClass {
        List<Person> persons() {
            return h2Person.findPersons();
        }

    }
}
