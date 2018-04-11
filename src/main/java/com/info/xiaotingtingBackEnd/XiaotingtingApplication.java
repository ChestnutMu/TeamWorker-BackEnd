package com.info.xiaotingtingBackEnd;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.info.xiaotingtingBackEnd.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    protected Logger logger = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(XiaotingtingApplication.class, args);
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer ssrv) {
        return new SpringAnnotationScanner(ssrv);
    }

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration configuration = new Configuration();
        configuration.setHostname("0.0.0.0");
        configuration.setPort(8092);
        configuration.setAuthorizationListener(new AuthorizationListener() {
            @Override
            public boolean isAuthorized(HandshakeData data) {
                try {
                    logger.info("连接身份认证: " + data.getAddress() + ":" + data.getUrl() + ":" + data.getUrlParams().size() + ":" + data.getHeaders() + ":" + data.getTime());

                    logger.info("连接数: " + socketIOServer().getAllClients().size());

                    String uid = data.getUrlParams().get("socketAuthorizationU").get(0);
                    String token = data.getUrlParams().get("socketAuthorizationT").get(0);

                    logger.info("身份认证开始 uid: " + uid + " token: " + token);
                    if (userService.idAuth(uid, token)) {
                        logger.info("身份认证成功");
                        return true;
                    } else {
                        logger.info("身份认证失败");
                        return false;
                    }
                } catch (Exception ex) {
                    logger.info("非法连接 地址: " + data.getAddress() + data.getUrl() + " 错误: " + ex);
                    //TODO 多次被非法访问应该禁止访问
                    return false;
                }
            }
        });
        final SocketIOServer server = new SocketIOServer(configuration);
        return server;
    }


}
