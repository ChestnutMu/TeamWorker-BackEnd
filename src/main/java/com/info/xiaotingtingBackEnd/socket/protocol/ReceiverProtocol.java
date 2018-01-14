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
    public static final int MSG_SEND_MESSAGE = 1002;//发消息
    public static final int MSG_ISREAD_MESSAGE = 1003;//已读
    public static final int MSG_ISSEND_MESSAGE = 1006;//已接收
}
