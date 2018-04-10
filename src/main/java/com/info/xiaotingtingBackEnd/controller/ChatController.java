package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.model.Chat;
import com.info.xiaotingtingBackEnd.model.ChatMessage;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.service.ChatService;
import com.info.xiaotingtingBackEnd.util.DataCheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/10 15:23:00
 * Description：
 * Email: xiaoting233zhang@126.com
 */

@RestController
@RequestMapping("chat")
public class ChatController {

    @Autowired
    ChatService chatService;

    /**
     * 创建聊天室
     *
     * @param userId
     * @param chat
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "buildChat", method = RequestMethod.POST)
    public ApiResponse<Chat> buildChat(@RequestHeader("uid") String userId, @RequestBody Chat chat) throws PlatformException {
        ApiResponse<Chat> apiResponse = new ApiResponse<>();
        chat.setAdminId(userId);
        chat = chatService.buildChat(chat);
        apiResponse.setData(chat);
        return apiResponse;
    }

    /**
     * 聊天室修改名字或图标或人员变动（群主的权限）
     *
     * @param userId
     * @param chat
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "changeChatInfo", method = RequestMethod.POST)
    public ApiResponse<Chat> changeChatInfo(@RequestHeader("uid") String userId, @RequestBody Chat chat) throws PlatformException {
        chat = chatService.changeChatInfo(userId, chat);
        ApiResponse<Chat> apiResponse = new ApiResponse<>();
        apiResponse.setData(chat);
        return apiResponse;
    }

    /**
     * 解散聊天室（群主的权限,且只有多人聊天室才能解散）
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "releaseChat", method = RequestMethod.POST)
    public ApiResponse<Object> releaseChat(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String chatId = params.get("chatId");
        chatService.releaseChat(userId, chatId);
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        return apiResponse;
    }

    /**
     * 获取聊天室信息
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "getChatInfo", method = RequestMethod.POST)
    public ApiResponse<Chat> getChatInfo(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String chatId = params.get("chatId");
        if (DataCheckUtil.isEmpty(chatId))
            throw new PlatformException(-1, "聊天室id不能为空");
        ApiResponse<Chat> apiResponse = new ApiResponse<>();
        Chat chat = chatService.findOne(chatId);
        if (!chat.getUserList().contains(userId))
            throw new PlatformException(-1, "你不在该聊天室");
        apiResponse.setData(chat);
        return apiResponse;
    }

    /**
     * 聊天室发送消息
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "sendChatMessage", method = RequestMethod.POST)
    public ApiResponse<Object> sendChatMessage(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String chatId = params.get("chatId");
        String message = params.get("message");
        if (DataCheckUtil.isEmpty(chatId))
            throw new PlatformException(-1, "聊天室id不能为空");
        if (DataCheckUtil.isEmpty(message))
            throw new PlatformException(-1, "消息不能为空");
        chatService.sendChatMessage(chatId, userId, message);
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        return apiResponse;
    }


}
