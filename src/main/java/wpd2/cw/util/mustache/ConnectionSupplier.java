package wpd2.cw.util.mustache;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by kat on 05/04/2018.
 */
public class ConnectionSupplier {
    public enum DB {
        MEMORY("jdbc:h2:mem:test"),
        FILE("\"jdbc:h2:~/test\"");
        private String file;
        DB(String file) {
            this.file = file;
        }
        String file() {
            return file;
        }
    }

    private final DB db;

    public ConnectionSupplier(DB db) {
        this.db = db;
    }

    public Connection provide() {
        try {
            Class.forName("org.h2.Driver");   // the driver class must be loaded so that DriverManager can find the loaded class
            return DriverManager.getConnection(db.file(), "sa", "");
        } catch (SQLException | ClassNotFoundException e) {
            throw new ConnectionSupplierException(e);
        }
    }

    public class ConnectionSupplierException extends RuntimeException {
        ConnectionSupplierException(Exception e) {
            super(e);
        }
    }
}
