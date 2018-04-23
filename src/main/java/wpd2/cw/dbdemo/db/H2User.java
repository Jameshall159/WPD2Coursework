
package wpd2.cw.dbdemo.db;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.cw.util.mustache.Password;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** IUserLogin via an H2 database */
public class H2User extends H2Base {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(H2User.class);


    public H2User(ConnectionSupplier connectionSupplier) {
        super(connectionSupplier.provide());
        try {
            initTable(getConnection());
        } catch (Exception e) {
            LOG.error("Can't find database driver: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void initTable(Connection conn) throws SQLException {
        execute(conn, "CREATE TABLE IF NOT EXISTS users (name VARCHAR(255) PRIMARY KEY, hash VARCHAR(255))");
    }


    public synchronized boolean login(@NonNull final String userName, @NonNull final String password) {
        errIfClosed();
        try {
            return loginSQL(userName, password);
        } catch (SQLException e) {
            LOG.error("Can't login " + userName + ": " + e.getMessage());
            return false;
        }
    }

    public synchronized boolean register(@NonNull final String userName, @NonNull final String password) {
        errIfClosed();
        try {
            return registerSQL(userName, password);
        } catch (SQLException e) {
            LOG.error("Can't register " + userName + ": " + e.getMessage());
            return false;
        }
    }

    public boolean isRegistered(String userName) {
        try (PreparedStatement ps = getConnection().prepareStatement("SELECT 1 FROM users WHERE name = ?")) {
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean loginSQL(String userName, String password) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement("SELECT hash FROM users WHERE name = ?")) {
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String hash = rs.getString("hash");
                return hash != null && validate(password, hash);
            }
        }
        return false;
    }

    private boolean registerSQL(String userName, String password) throws SQLException {
        String hash = hash(password);
        if (hash == null) {
            return false;
        }
        if (hasUserSQL(userName)) {
            return false;
        }
        String query = "INSERT into users (name, hash) VALUES(?,?)";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, userName);
            ps.setString(2, hash);
            int count = ps.executeUpdate();
            LOG.debug("insert count = " + count);
            return count == 1;
        }
    }


    private boolean hasUserSQL(String userName) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement("SELECT name FROM users WHERE name = ?")) {
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    private boolean validate(String password, String hash) {
        try {
            return Password.validatePassword(password, hash);
        } catch (Password.PasswordException e) {
            LOG.error("Can't validate password: " + e.getMessage());
            return false;
        }
    }

    private String hash(String password) {
        try {
            return Password.createHash(password);
        } catch (Password.PasswordException e) {
            LOG.error("Can't hash password <" + password + ">: " + e.getMessage());
            return null;
        }
    }
}
