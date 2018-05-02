package com.info.xiaotingtingBackEnd.socket.protocol;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/31 09:43:53
 * Description：接收客户端消息协议
 * Email: xiaoting233zhang@126.com
 */
public class ReceiverProtocol {
    /**
     * msgId 协议
     */
    public static final int MSG_CONNECTONION_MESSAGE = 1001;//连接

    public static final int MSG_ISSEND_MESSAGE = 1006;//已接收聊天室消息

    public static final int MSG_ISSEND_NOTIFICATION = 3001;//已接收通知

    public static final int MSG_ISSEND_CHAT_MESSAGE = 2000;//已接收聊天室消息 发送对象 chatMessageId
}
