package com.info.xiaotingtingBackEnd.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.gson.Gson;
import com.info.xiaotingtingBackEnd.socket.protocol.SenderProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/11 16:34:41
 * Description：Socket连接基类
 * Email: xiaoting233zhang@126.com
 */
public class BaseSocketEventHandler {
    protected Logger logger = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    protected final SocketIOServer server;

    protected static final String TAG_USER_SEND_MESSAGE = "tag_user_send_message";

    protected static final String TAG_USER_RECEIVER_MESSAGE = "tag_user_receiver_message";

    protected static final String TAG_USER_RECEIVER_NOTIFICATION = "tag_user_receiver_notification";

    protected static Map<String, SocketIOClient> clientHashMap = new ConcurrentHashMap<>();

    protected Gson gson = new Gson();


    @Autowired
    public BaseSocketEventHandler(SocketIOServer server) {
        this.server = server;
    }

    /**
     * 通过uid和token下线客户端
     * 成功踢下线则返回true
     * 失败则返回false
     *
     * @param uid   用户uid
     * @param token 当前token
     */
    public boolean kickOffClient(String uid, String token) {
        logger.info("通过uid和token下线客户端 uid: " + uid + " token: " + token);
        SocketIOClient uidSocket = clientHashMap.get(uid);
        if (uidSocket != null) {
            String tokenTemp = uidSocket.get("token");
            logger.info("检查token uid: " + uid + " token: " + tokenTemp);
            if (tokenTemp.equals(token)) {
                logger.info("踢下线: 找到 uid：" + uid + " token: " + tokenTemp);
                kickOffClient(uidSocket);
                return true;
            }
        }
        return false;
    }

    /**
     * 直接下线客户端
     *
     * @param client 用户uid
     */
    public void kickOffClient(SocketIOClient client) {
        logger.info("直接下线客户端: 找到 uid：" + client.get("uid") + " token: " + client.get("token"));
        client.sendEvent(TAG_USER_RECEIVER_MESSAGE, SenderProtocol.DUPLICATE_LOGIN, "kickOff");
        //服务器主动下线客户端
        client.disconnect();
    }

}
