package wpd2.cw.dbdemo.db;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.cw.dbdemo.model.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class MemMessages implements IMessageDB {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(MemMessages.class);

    private final List<Message> messages;
    private long index;

    public MemMessages() {
        messages = new ArrayList<>();
        index = 0;
    }

    @Override
    public synchronized Message get(long id) {
        for (Message m: messages) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    @Override
    public synchronized List<Message> list() {
        return Collections.unmodifiableList(messages);
    }

    @Override
    public synchronized void add(@NonNull String message, String user) {
        Message m = new Message(index++, message, user, new Date().getTime());
        messages.add(m);
    }

    @Override
    public synchronized List<Message> user(@NonNull String user) {
        List<Message> out = messages.stream().filter(m -> user.equals(m.getUser())).collect(Collectors.toList());
        return Collections.unmodifiableList(out);
    }

    @Override
    public synchronized void delete(long id) {
        for (Message m: messages) {
            if (id == m.getId()) {
                messages.remove(m);
                return;
            }
        }
    }
}
