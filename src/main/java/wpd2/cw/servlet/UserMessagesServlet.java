// Copyright (c) 2018 Katrin Hartmann. All Rights Reserved.
//
// File:        UserMessagesServlet.java
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


package wpd2.cw.servlet;

import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.cw.dbdemo.db.H2User;
import wpd2.cw.dbdemo.db.IMessageDB;
import wpd2.cw.dbdemo.model.Message;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Singleton
public class UserMessagesServlet extends BaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(UserMessagesServlet.class);

    private static final String USER_MESSAGES_TEMPLATE = "userMessages.mustache";
    private static final String MESSAGE_PARAMETER = "message";
    private static final String METHOD_PARAMETER = "method";
    private static final String ID_PARAMETER = "msgId";

    private final IMessageDB db;
    private final H2User userDB;

    @Inject
    public UserMessagesServlet(IMessageDB db, H2User userDB) {
        this.db = db;
        this.userDB = userDB;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authOK(request, response)) {
            return;
        }
        String userName = userFromRequest(request);
        String loggedInUser = UserFuncs.getCurrentUser(request);
        if (!userDB.isRegistered(userName)) {
            String err = "can't find user " + userName;
            issue("text/plain", HttpServletResponse.SC_NOT_FOUND, err.getBytes(Charsets.UTF_8), response);
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("user", userName);
        map.put("matches", loggedInUser.equals(userName));

        List<Message> messages = db.user(userName);
        if (messages.size() > 0) {
            map.put("messages", messages);
        }
        showView(response, USER_MESSAGES_TEMPLATE, map);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authOK(request, response)) {
            return;
        }
        String method = getString(request, METHOD_PARAMETER, "post");
        if ("delete".equals(method)) {
            doDelete(request, response);
        } else {
            String message = request.getParameter(MESSAGE_PARAMETER);
            String userName = userFromRequest(request);
            if (!userDB.isRegistered(userName)) {
                String err = "can't find user " + userName;
                issue("text/plain", HttpServletResponse.SC_NOT_FOUND, err.getBytes(Charsets.UTF_8), response);
                return;
            }
            db.add(message, userName);
            response.sendRedirect(response.encodeRedirectURL(request.getRequestURI()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authOK(request, response)) {
            return;
        }
        String loggedInUser = UserFuncs.getCurrentUser(request);
        long id = getLong(request, ID_PARAMETER);
        Message m = db.get(id);
        if (m != null && loggedInUser.equals(m.getUser())) {
            db.delete(id);
        }
        response.sendRedirect(response.encodeRedirectURL(request.getRequestURI()));
    }

    static String userFromRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String[] sub = uri.split("/");
        if (sub.length == 3) {
            return sub[2];
        }
        return "";
    }

}
