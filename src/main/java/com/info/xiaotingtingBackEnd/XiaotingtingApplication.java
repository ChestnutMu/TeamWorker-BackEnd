package com.info.xiaotingtingBackEnd;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/11 15:41:52
 * Description：启动SpringBoot
 * Email: xiaoting233zhang@126.coma
 */

@SpringBootApplication
@ImportResource("classpath:applicationContext.xml")
public class XiaotingtingApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaotingtingApplication.class, args);
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer ssrv) {
        return new SpringAnnotationScanner(ssrv);
    }

    @Bean
    public SocketIOServer socketIOServer(){
        Configuration configuration=new Configuration();
        configuration.setHostname("0.0.0.0");
        configuration.setPort(8092);
        configuration.setAuthorizationListener(new AuthorizationListener() {
            @Override
            public boolean isAuthorized(HandshakeData handshakeData) {
                return true;
            }
        });
        final SocketIOServer server=new SocketIOServer(configuration);
        return server;
    }


}
