// Copyright (c) 2018 Katrin Hartmann. All Rights Reserved.
//
// File:        Message.java
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


package wpd2.cw.dbdemo.model;

import lombok.NonNull;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@Value
public class Message {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(Message.class);

    private long id;
    private String message;
    private String user;
    private long created;

    public Message(long id, @NonNull String message, @NonNull String user, long created) {
        this.id = id;
        this.message = message;
        this.user = user;
        this.created = created;
    }

    public String getCreatedDate() {
        return new Date(created).toString();
    }

}
