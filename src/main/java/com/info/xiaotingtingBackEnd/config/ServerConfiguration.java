package com.info.xiaotingtingBackEnd.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/11 16:21:47
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Component
public class ServerConfiguration implements CommandLineRunner {
    protected Logger logger = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private final SocketIOServer server;

    @Autowired
    public ServerConfiguration(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void run(String... args) throws Exception {
        server.start();
//        Thread.sleep(Integer.MAX_VALUE);
//        server.stop();
    }
}
