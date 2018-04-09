package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.NewFriendRequest;
import com.info.xiaotingtingBackEnd.model.vo.NewFriendRequestVo;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.service.NewFriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 请求加好友
 * 获取请求好友信息大小（未获取过）
 * 获取请求好友列表（未获取过）
 * 同意添加好友
 * <p>
 * <p>
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/6 19:49:20
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@RestController
@RequestMapping("user")
public class NewFriendRequestController {

    @Autowired
    NewFriendRequestService requestService;


    /**
     * 加好友请求
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "sendFriendRequest", method = RequestMethod.POST)
    public ApiResponse<Object> sendFriendRequest(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String recipientId = params.get("recipientId");
        String message = params.get("message");
        requestService.sendFriendRequest(userId, recipientId, message);
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("添加好友请求成功");
        return apiResponse;
    }


    /**
     * 根据userId获取其还未接收成功的好友请求消息的数量
     *
     * @return
     */
    @RequestMapping(value = "countNotSendRequestByUserId", method = RequestMethod.POST)
    public ApiResponse<Long> countNotSendRequestByUserId(@RequestHeader("uid") String userId) {
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        long count = requestService.countNotSendRequestByUserId(userId);
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("获取未接收消息列表的数量成功");
        apiResponse.setData(count);
        return apiResponse;
    }


    /**
     * 根据userId其接收到的好友请求消息列表
     *
     * @return
     */
    @RequestMapping(value = "getRequestByUserId", method = RequestMethod.POST)
    public ApiResponse<List<NewFriendRequestVo>> getRequestByUserId(@RequestHeader("uid") String userId) {
        ApiResponse<List<NewFriendRequestVo>> apiResponse = new ApiResponse<>();
        List<NewFriendRequestVo> requestList = requestService.getRequestVoByUserId(userId);
        apiResponse.setMaxCount(requestList.size());
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("获取消息列表成功");
        apiResponse.setData(requestList);
        return apiResponse;
    }

    /**
     * 设置接受该好友请求
     *
     * @return
     */
    @RequestMapping(value = "acceptedRequest", method = RequestMethod.POST)
    public ApiResponse<Object> acceptedRequest(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String newFriendRequestId = params.get("newFriendRequestId");
        requestService.acceptedRequest(userId, newFriendRequestId);
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("已接收该好友请求");
        return apiResponse;
    }


}
