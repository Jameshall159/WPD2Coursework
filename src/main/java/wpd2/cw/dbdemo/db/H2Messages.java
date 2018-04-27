package wpd2.cw.dbdemo.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.cw.dbdemo.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class H2Messages extends H2Base implements IMessageDB {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(H2Messages.class);

    public static String randomString(int length){
        StringBuilder b = new StringBuilder();
        for(int i = 0; i < length; i++){
            b.append(base.charAt(random.nextInt(base.length())));
        }
        return b.toString();
    }

    private static String base = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabsdefghijklmnopqrstuvwxyz";
    private static Random random = new Random();

    public H2Messages(ConnectionSupplier connectionSupplier) {
        super(connectionSupplier.provide());
        try {
            initTable(getConnection());
        } catch (Exception e) {
            LOG.error("Can't find database driver: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void initTable(Connection conn) throws SQLException {
        execute(conn, "CREATE TABLE IF NOT EXISTS " +
                "messages (id BIGINT AUTO_INCREMENT, message VARCHAR(255), description VARCHAR(255), user VARCHAR(255), " +
                "created BIGINT, expectedComplete VARCHAR(255), actual INT(2), link VARCHAR(255) PRIMARY KEY(id))");
    }

    @Override
    public Message get(long id) {
        String ps = "SELECT id, message, description, user, created, expectedComplete, actual, link FROM messages where id = ?";
        Connection conn = getConnection();
        try {
            PreparedStatement p = conn.prepareStatement(ps);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                return rs2message(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new H2MessagesException(e);
        }
    }

    @Override
    public List<Message> list() {
        String ps = "SELECT id, message, description, user, created, expectedComplete, actual, link FROM messages";
        Connection conn = getConnection();
        try {
            List<Message> out = new ArrayList<>();
            PreparedStatement p = conn.prepareStatement(ps);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                Message m = rs2message(rs);
                out.add(m);
            }
            return out;
        } catch (SQLException e) {
            throw new H2MessagesException(e);
        }
    }

    @Override
    public void add(String message, String description, String user, String expectedComplete, int actual, String link) {
        String ps = "INSERT INTO messages (message, description, user, created, expectedComplete, actual, link) VALUES(?,?, ?,?,?,?,?)";
        Connection conn = getConnection();
        java.util.Date d = new java.util.Date();

        try (PreparedStatement p = conn.prepareStatement(ps)) {
            p.setString(1, message);
            p.setString(2, description);
            p.setString(3, user);
            p.setLong(4, d.getTime());
            p.setString(5, expectedComplete);
            p.setInt(6,  actual);
            p.setString(7, link);
            p.execute();
        } catch (SQLException e) {
            throw new H2MessagesException(e);
        }
    }

    @Override
    public List<Message> user(String user) {
        String ps = "SELECT id, message, user, created FROM messages WHERE user = ?";
        Connection conn = getConnection();
        try {
            List<Message> out = new ArrayList<>();
            PreparedStatement p = conn.prepareStatement(ps);
            p.setString(1, user);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                Message m = rs2message(rs);
                out.add(m);
            }
            return out;
        } catch (SQLException e) {
            throw new H2MessagesException(e);
        }
    }

    @Override
    public List<Message> link(String link) {
        String ps = "SELECT id, message, user, created, link FROM messages WHERE link = ?";
        Connection conn = getConnection();
        try {
            List<Message> out = new ArrayList<>();
            PreparedStatement p = conn.prepareStatement(ps);
            p.setString(1, link);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                Message m = rs2message(rs);
                out.add(m);
            }
            return out;
        } catch (SQLException e) {
            throw new H2MessagesException(e);
        }
    }


    @Override
    public void delete(long id) {
        Connection conn = getConnection();
        String ps = "DELETE FROM messages WHERE id = ?";
        try (PreparedStatement p = conn.prepareStatement(ps)) {
            p.setLong(1, id);
            p.execute();
        } catch (SQLException e) {
            throw new H2MessagesException(e);
        }
    }

    @Override
    public void update(long id, String message, String description, String user, String expectedComplete, int actual, String link) {
        Connection conn = getConnection();
        java.util.Date d = new java.util.Date();
        String ps = "UPDATE messages SET message = ?, description = ?, user = ?, expectedComplete = ?, actual = ?, link = ? WHERE id = ?";
        try (PreparedStatement p = conn.prepareStatement(ps)) {
            p.setString(1, message);
            p.setString(2, description);
            p.setString(3, user);
            p.setLong(4, d.getTime());
            p.setString(5, expectedComplete);
            p.setInt(6,  actual);
            p.setString(7, link);
            p.execute();
        } catch (SQLException e) {
            throw new H2MessagesException(e);
        }
    }

    public boolean postExists(String link) {
        try (PreparedStatement ps = getConnection().prepareStatement("SELECT 1 FROM messages WHERE link = ?")) {
            ps.setString(1, link);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    private static Message rs2message(ResultSet rs) throws SQLException {
        return new Message(rs.getLong(1), rs.getString(2),
                rs.getString(3), rs.getString(4), rs.getLong(5), rs.getString(6),
                rs.getInt(7), rs.getString(8));
    }

    public static final class H2MessagesException extends RuntimeException {
        H2MessagesException(Exception e) {
            super(e);
        }
    }

}
