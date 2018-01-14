package com.info.xiaotingtingBackEnd.socket.protocol;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/31 09:43:53
 * Description：发送客户端消息协议
 * Email: xiaoting233zhang@126.com
 */
public class SenderProtocol {

    /**
     * 账号在其他设备登录，强制下线
     */
    public static final int DUPLICATE_LOGIN = 1004;

    /**
     * 发送客户端一条新消息
     */
    public static final int MSG_SEND_NORMAL_MESSAGE = 1005;


}
