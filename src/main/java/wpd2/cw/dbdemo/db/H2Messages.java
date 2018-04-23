// Copyright (c) 2018 Katrin Hartmann. All Rights Reserved.
//
// File:        H2Messages.java
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
import wpd2.cw.dbdemo.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class H2Messages extends H2Base implements IMessageDB {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(H2Messages.class);


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
                "messages (id BIGINT AUTO_INCREMENT, message VARCHAR(255), user VARCHAR(255), " +
                "created BIGINT, PRIMARY KEY(id))");
    }

    @Override
    public Message get(long id) {
        String ps = "SELECT id, message, user, created FROM messages where id = ?";
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
        String ps = "SELECT id, message, user, created FROM messages";
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
    public void add(String message, String user) {
        String ps = "INSERT INTO messages (message, user, created) VALUES(?,?, ?)";
        Connection conn = getConnection();
        try (PreparedStatement p = conn.prepareStatement(ps)) {
            p.setString(1, message);
            p.setString(2, user);
            p.setLong(3, new Date().getTime());
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

    private static Message rs2message(ResultSet rs) throws SQLException {
        return new Message(rs.getLong(1), rs.getString(2),
                rs.getString(3), rs.getLong(4));
    }

    public static final class H2MessagesException extends RuntimeException {
        H2MessagesException(Exception e) {
            super(e);
        }
    }

}
