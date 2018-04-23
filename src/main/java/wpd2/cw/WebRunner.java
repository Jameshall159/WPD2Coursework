package wpd2.cw;// Copyright (c) 2018 Katrin Hartmann. All Rights Reserved.
//
// File:        WebRunner.java
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


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WebRunner {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(WebRunner.class);

    public WebRunner() {

    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(9001);

        String rootPath = WebRunner.class.getClassLoader().getResource(".").toString();
        WebAppContext webapp = new WebAppContext(rootPath + "webapp", "");
        server.setHandler(webapp);

        server.start();
        server.join();
    }
}
