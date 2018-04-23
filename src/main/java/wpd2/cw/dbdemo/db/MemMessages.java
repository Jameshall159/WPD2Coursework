// Copyright (c) 2018 Katrin Hartmann. All Rights Reserved.
//
// File:        MemMessages.java
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

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.cw.dbdemo.model.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class MemMessages implements IMessageDB {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(MemMessages.class);

    private final List<Message> messages;
    private long index;

    public MemMessages() {
        messages = new ArrayList<>();
        index = 0;
    }

    @Override
    public synchronized Message get(long id) {
        for (Message m: messages) {
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
    public synchronized void add(@NonNull String message, String user) {
        Message m = new Message(index++, message, user, new Date().getTime());
        messages.add(m);
    }

    @Override
    public synchronized List<Message> user(@NonNull String user) {
        List<Message> out = messages.stream().filter(m -> user.equals(m.getUser())).collect(Collectors.toList());
        return Collections.unmodifiableList(out);
    }

    @Override
    public synchronized void delete(long id) {
        for (Message m: messages) {
            if (id == m.getId()) {
                messages.remove(m);
                return;
            }
        }
    }
}
