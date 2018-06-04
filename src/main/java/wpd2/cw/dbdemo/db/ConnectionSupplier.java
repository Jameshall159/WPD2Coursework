package wpd2.cw.dbdemo.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by kat on 05/04/2018.
 */
public class ConnectionSupplier {
    public static final String MEMORY = "jdbc:h2:mem:test";
    public static final String FILE = "jdbc:h2:~/test";

    private final String db;

    public ConnectionSupplier(String db) {
        this.db = db;
    }

    public Connection provide() {
        try {
            // the driver class must be loaded
            // so that DriverManager can find the loaded class
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection(db, "sa", "");
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
