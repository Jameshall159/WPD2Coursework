// Copyright (c) 2018 Katrin Hartmann. All Rights Reserved.
//
// File:        MessagesServlet.java
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Random;

@Singleton
public class addMilestoneServlet extends BaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(addMilestoneServlet.class);
    java.util.Date d = new java.util.Date();
    private static final String MESSAGES_TEMPLATE = "addmilestone.mustache";
    private static final String MESSAGE_PARAMETER = "message";
    private static final String DESCRIPTION_PARAMETER = "description";
    private static final String EXPECTED_PARAMETER = "expectedComplete";

    private final IMessageDB db;



    @Inject
    public addMilestoneServlet(IMessageDB db) {
        this.db = db;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authOK(request, response)) {
            return;
        }
        List<Message> messages = db.list();
        Map<String,Object> map = new HashMap<>();
        String userName = UserFuncs.getCurrentUser(request);
        map.put("user", userName);
        if (messages.size() > 0) {
            map.put("messages", messages);
        }
        showView(response, MESSAGES_TEMPLATE, map);
    }

    public static String randomString(int length){
        StringBuilder b = new StringBuilder();
        for(int i = 0; i < length; i++){
            b.append(base.charAt(random.nextInt(base.length())));
        }
        return b.toString();
    }

    String link5 = randomString(7);

    private static String base = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabsdefghijklmnopqrstuvwxyz";
    private static Random random = new Random();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authOK(request, response)) {
            return;
        }
        String message = request.getParameter(MESSAGE_PARAMETER);
        String description = request.getParameter(DESCRIPTION_PARAMETER);
        int actual = 0;
        String expectedComplete = request.getParameter(EXPECTED_PARAMETER);
        String user = UserFuncs.getCurrentUser(request);
        String link = link5;
        db.add(message, description, user, expectedComplete, actual, link);
        response.sendRedirect(response.encodeRedirectURL(request.getRequestURI()));
    }

}
