package wpd2.cw.servlet;

import lombok.Data;
import wpd2.cw.dbdemo.db.H2Person;
import wpd2.cw.dbdemo.model.Person;
import wpd2.cw.util.mustache.MustacheRender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;


public class PersonServlet extends HttpServlet{
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(PersonServlet.class);

    private final H2Person h2Person;
    private final MustacheRender mustache;
    public PersonServlet(H2Person h2Person) {
        mustache = new MustacheRender();
        this.h2Person = h2Person;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Person> persons = h2Person.findPersons();
        String html = mustache.render("index.mustache", new Result(persons.size()));
        response.setContentType("text/html");
        response.setStatus(200);
        response.getOutputStream().write(html.getBytes(Charset.forName("utf-8")));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String first = request.getParameter("first");
        String last = request.getParameter("last");
        String email = request.getParameter("email");
        Person person = new Person(first, last, email);
        h2Person.addPerson(person);
        response.sendRedirect("/index.html");
    }

    @Data
    class Result {
        private int count;
        Result(int count) { this.count = count; }
    }
}

