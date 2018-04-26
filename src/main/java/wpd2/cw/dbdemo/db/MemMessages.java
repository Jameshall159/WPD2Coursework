package wpd2.cw.dbdemo.db;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.cw.dbdemo.model.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;


public class MemMessages implements IMessageDB {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(MemMessages.class);
    java.util.Date d = new java.util.Date();

    private final List<Message> messages;
    private long index;

    public MemMessages() {
        messages = new ArrayList<>();
        index = 0;
    }

    @Override
    public synchronized Message get(long id) {
        for (Message m : messages) {
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
    public synchronized void add(@NonNull String message, String description, String user, String expectedComplete, int actual, String link) {
        Message m = new Message(index++, message, description, user, d.getTime(), expectedComplete, actual, link);
        messages.add(m);
    }

    @Override
    public synchronized List<Message> user(@NonNull String user) {
        List<Message> out = messages.stream().filter(m -> user.equals(m.getUser())).collect(Collectors.toList());
        return Collections.unmodifiableList(out);
    }

    @Override
    public synchronized void delete(long id) {
        for (Message m : messages) {
            if (id == m.getId()) {
                messages.remove(m);
                return;
            }
        }
    }

    @Override
    public synchronized void update(@NonNull long id, String message, String description, String user, String expectedComplete, int actual, String link ) {
        for (Message m : messages) {
            if (id == m.getId()) {
                Message me = new Message(index++, message, description, user, new Date().getTime(), expectedComplete, actual, link);
                messages.set(1, me);
                return;
            }
        }
    }
}