package wpd2.cw.dbdemo.model;

import lombok.Data;
import lombok.NonNull;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Value
public class Person {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(Person.class);

    private final String first;
    private final String last;
    private final String email;

    public Person(String first, String last, String email) {
        this.first = first;
        this.last = last;
        this.email = email;
    }
    public String getFirst() {
        return first;
    }

    public String getLast() {
        return last;
    }

    public String getEmail() {
        return email;
    }
}
