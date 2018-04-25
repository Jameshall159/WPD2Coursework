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
    private String user;
    private long created;

    public Message(long id, @NonNull String message, @NonNull String user, long created) {
        this.id = id;
        this.message = message;
        this.user = user;
        this.created = created;
    }

    public String getCreatedDate() {
        return new Date(created).toString();
    }

}
