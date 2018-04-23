// Copyright (c) 2018 Katrin Hartmann. All Rights Reserved.
//
// File:        H2Base.java
//
// Copyright in the whole and every part of this source file belongs to
// Cilogi (the Author) and may not be used, sold, licenced, 
// transferred, copied or reproduced in whole or in part in 
// any manner or form or in or on any media to any person other than 
// in accordance with the terms of The Author's agreement
// or otherwise without the prior written consent of The Author.  All
// information contained in this source file is confidential information
// belonging to The Author and as such may not be disclosed other
// than in accordance with the terms of The Author's agreement, or
// otherwise, without the prior written consent of The Author.  As
// confidential information this source file must be kept fully and
// effectively secure at all times.
//


package wpd2.cw.dbdemo.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


class H2Base implements AutoCloseable {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(H2Base.class);

    private Connection connection;

    H2Base(Connection connection) {
        this.connection = connection;
    }

    @Override
    public synchronized void close()throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    protected Connection getConnection() {
        return connection;
    }

    protected static void execute(Connection connection, String cmd) {
        try {
            Statement statement = null;
            try {
                statement = connection.createStatement();
                statement.execute(cmd);
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    protected void errIfClosed() {
        if (getConnection() == null) {
            throw new NullPointerException("H2 connection is closed");
        }
    }
}
