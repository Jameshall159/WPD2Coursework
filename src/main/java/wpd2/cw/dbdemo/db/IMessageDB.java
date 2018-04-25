package wpd2.cw.dbdemo.db;

import wpd2.cw.dbdemo.model.Message;

import java.util.List;

public interface IMessageDB {
    /**
     * Get message with given id
     * @param id the id to look up
     * @return the message or null if there is none
     */
    Message get(long id);

    /**
     * List all messages
     * @return A list of all messages, or an empty list if there are none
     */
    List<Message> list();

    /**
     * Add a message from a given user.  The timestamp will be <code>new Date().getTime()</code>
     * @param message  Message text (not null)
     * @param user User name (not null)
     */
    void add(String message, String user);

    /**
     * List messages for a given user
     * @param user  User name (not null)
     * @return  The messages from this user, or the empty list if none
     */
    List<Message> user(String user);

    /**
     * Delete message with the given id, if it exists
     * @param id The id
     */
    void delete(long id);


    void update(long id, String message, String user);
}