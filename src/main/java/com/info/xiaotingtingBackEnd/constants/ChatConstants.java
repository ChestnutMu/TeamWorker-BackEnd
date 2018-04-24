package com.info.xiaotingtingBackEnd.constants;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/10 15:23:00
 * Description：
 * Email: xiaoting233zhang@126.com
 */
public class ChatConstants {
    /**
     * 聊天室类型 0双人聊天室 1多人聊天室
     */
    public static final int TYPE_CHAT_DOUBLE = 0;
    public static final int TYPE_CHAT_MULTIPLAYER = 1;

    /**
     * 消息类型 null或者0 默认普通消息 1修改聊天室名称 2修改聊天室头像 3人员变动-添加 4人员变动-移除 5退出聊天室
     */
    public static final int TYPE_MESSAGE_NORMAL = 0;
    public static final int TYPE_MESSAGE_CHANGE_NAME = 1;
    public static final int TYPE_MESSAGE_CHANGE_PIC = 2;
    public static final int TYPE_MESSAGE_CHANGE_PEOPLE_ADD = 3;
    public static final int TYPE_MESSAGE_CHANGE_PEOPLE_REMOVE = 4;
    public static final int TYPE_MESSAGE_PEOPLE_OUT = 5;
}
