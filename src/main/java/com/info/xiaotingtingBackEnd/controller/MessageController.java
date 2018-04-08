package com.info.xiaotingtingBackEnd.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.Message;
import com.info.xiaotingtingBackEnd.model.vo.MessageVo;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.repository.base.SearchBean;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/31 09:21:55
 * Description：MessageController
 * Email: xiaoting233zhang@126.com
 */
@RestController
@RequestMapping("message")
public class MessageController {

    @Autowired
    MessageService messageService;

    /**
     * 群发通知或聊天消息
     *
     * @return
     */
    // TODO: 2018/4/8 添加一个发送通知、公告的接口，并且加入权限验证的WebMvcConfigurer类中
    @RequestMapping(value = "sendMessage", method = RequestMethod.POST)
    public ApiResponse<Object> sendMessage(@RequestBody Map<String, String> params) {
        String chatId = params.get("chatId");
        String chatName = params.get("chatName");
        String userId = params.get("userId");
        String content = params.get("content");
        String uids = params.get("uids");
        List<String> uidList = new Gson().fromJson(uids, new TypeToken<List<String>>() {
        }.getType());

        messageService.sendMessage(chatId, chatName, userId, content, uidList);

        ApiResponse<Object> apiResponse = new ApiResponse<>();

        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("发送成功");
        return apiResponse;
    }

    /**
     * 设置消息已接收
     *
     * @return
     */
    @RequestMapping(value = "hasSendMessage", method = RequestMethod.POST)
    public ApiResponse<Object> hasSendMessage(@RequestBody Map<String, String> params) {
        String messageId = params.get("messageId");

        messageService.hasSendMessage(messageId);

        ApiResponse<Object> apiResponse = new ApiResponse<>();

        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("设置该消息已接收");
        return apiResponse;
    }

    /**
     * 设置消息已读
     *
     * @return
     */
    @RequestMapping(value = "hasReadMessage", method = RequestMethod.POST)
    public ApiResponse<Object> hasReadMessage(@RequestBody Map<String, String> params) {
        String messageId = params.get("messageId");

        messageService.hasReadMessage(messageId);

        ApiResponse<Object> apiResponse = new ApiResponse<>();

        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("设置该消息已读");
        return apiResponse;
    }

    /**
     * 根据userId获取其收到的消息，每个senderId都只取其最新一条消息
     *
     * @return
     */
    @RequestMapping(value = "getTopMessagesByUserId", method = RequestMethod.POST)
    public ApiResponse<List<MessageVo>> getTopMessagesByUserId(@RequestBody Map<String, Object> params) {
        String userId = (String) params.get("userId");
        int pageNum = (int) params.get("pageNum");
        int pageSize = (int) params.get("pageSize");
        ApiResponse<List<MessageVo>> apiResponse = new ApiResponse<>();
        Pageable pageable = new PageRequest(pageNum - 1, pageSize);
        Page<MessageVo> messageList = messageService.getTopMessagesByUserId(userId, pageable);
        apiResponse.setCurrentPage(pageNum);
        apiResponse.setPageSize(pageSize);
        apiResponse.setMaxCount((int) messageList.getTotalElements());
        apiResponse.setMaxPage(messageList.getTotalPages());
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("获取消息列表成功");
        apiResponse.setData(messageList.getContent());
        return apiResponse;
    }

    /**
     * 根据userId获取其还未接收成功的消息
     *
     * @return
     */
    @RequestMapping(value = "getNotSendMessageVosByUserId", method = RequestMethod.POST)
    public ApiResponse<List<MessageVo>> getNotSendMessageVosByUserId(@RequestBody Map<String, Object> params) {
        String userId = (String) params.get("userId");
        ApiResponse<List<MessageVo>> apiResponse = new ApiResponse<>();
        List<MessageVo> messageList = messageService.getNotSendMessageVosByUserId(userId);
        apiResponse.setMaxCount(messageList.size());
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("获取未接收消息列表成功");
        apiResponse.setData(messageList);
        return apiResponse;
    }

    /**
     * 根据userId获取其还未接收成功的消息
     *
     * @return
     */
    @RequestMapping(value = "getNotSendMessagesByUserId", method = RequestMethod.POST)
    public ApiResponse<List<Message>> getNotSendMessagesByUserId(@RequestBody Map<String, Object> params) {
        String userId = (String) params.get("userId");
        ApiResponse<List<Message>> apiResponse = new ApiResponse<>();
        List<Message> messageList = messageService.getNotSendMessagesByUserId(userId);
        apiResponse.setMaxCount(messageList.size());
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("获取未接收消息列表成功");
        apiResponse.setData(messageList);
        return apiResponse;
    }

}
