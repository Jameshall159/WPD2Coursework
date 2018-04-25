package wpd2.cw.dbdemo.model;

import lombok.NonNull;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@Value
public class Message {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(Message.class);

    private long id;
    private String message;
    private String description;
    private String user;
    private long created;
    private long expectedComplete;
    private int actual;
    private String link;

    public Message(long id, @NonNull String message, String description, @NonNull String user, long created, long expectedComplete,
                   int actual, String link) {
        this.id = id;
        this.message = message;
        this.description = description;
        this.user = user;
        this.created = created;
        this.expectedComplete = expectedComplete;
        this.actual = actual;
        this.link = link;
    }

    public String getCreatedDate() {
        return new Date(created).toString();
    }

}
