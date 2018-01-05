package com.info.xiaotingtingBackEnd.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.Message;
import com.info.xiaotingtingBackEnd.model.vo.MessageVo;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/31 09:21:55
 * Description：MessageController
 * Email: xiaoting233zhang@126.com
 */
@RestController
@RequestMapping("Message")
public class MessageController {

    @Autowired
    MessageService messageService;

    /**
     * 发送消息
     *
     * @param userId
     * @param title
     * @param content
     * @param uids
     * @return
     */
    @RequestMapping(value = "sendMessage", method = RequestMethod.POST)
    public ApiResponse<Object> sendMessage(@RequestHeader("userId") String userId, String title, String content, String uids) {
        List<String> uidList = new Gson().fromJson(uids, new TypeToken<List<String>>() {
        }.getType());

        messageService.sendMessage(userId, title, content, uidList);

        ApiResponse<Object> apiResponse = new ApiResponse<>();

        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("发送成功");
        return apiResponse;
    }


    /**
     * 设置消息已读
     *
     * @param userId
     * @param messageId
     * @return
     */
    @RequestMapping(value = "hasReadMessage", method = RequestMethod.POST)
    public ApiResponse<Object> hasReadMessage(@RequestHeader("userId") String userId, String messageId) {

        messageService.hasReadMessage(messageId);

        ApiResponse<Object> apiResponse = new ApiResponse<>();

        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("设置该消息已读");
        return apiResponse;
    }

    /**
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "getMessagesByUserId", method = RequestMethod.POST)
    public ApiResponse<List<MessageVo>> getMessagesByUserId(String userId, int pageNum, int pageSize) {
        ApiResponse<List<MessageVo>> apiResponse = new ApiResponse<>();
        Pageable pageable = new PageRequest(pageNum - 1, pageSize);
        Page<MessageVo> messageList = messageService.getMessagesByUserId(userId, pageable);
        apiResponse.setCurrentPage(pageNum);
        apiResponse.setPageSize(pageSize);
        apiResponse.setMaxCount((int) messageList.getTotalElements());
        apiResponse.setMaxPage(messageList.getTotalPages());
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("获取消息列表成功");
        apiResponse.setData(messageList.getContent());
        return apiResponse;
    }

}
