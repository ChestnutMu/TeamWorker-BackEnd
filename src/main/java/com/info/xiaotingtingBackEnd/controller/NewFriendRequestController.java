package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.NewFriendRequest;
import com.info.xiaotingtingBackEnd.model.vo.NewFriendRequestVo;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.service.NewFriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
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
     * 设置好友请求消息已接收
     *
     * @return
     */
    @RequestMapping(value = "hasSendRequest", method = RequestMethod.POST)
    public ApiResponse<Object> hasSendRequest(@RequestBody Map<String, String> params) {
        String requestId = params.get("requestId");
        requestService.hasSendRequest(requestId);
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("好友请求消息已接收");
        return apiResponse;
    }

    /**
     * 设置接受该好友请求
     *
     * @return
     */
    @RequestMapping(value = "isAccepted", method = RequestMethod.POST)
    public ApiResponse<Object> isAccepted(@RequestBody Map<String, String> params) {
        String requestId = params.get("requestId");
        String isAccepted = params.get("isAccepted");
        if (isAccepted.equals("接受")) {
            requestService.isAccepted(requestId);
        }
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("已接收该好友请求");
        return apiResponse;
    }

    /**
     * 根据userId获取其还未接收成功的好友请求消息
     *
     * @return
     */
    @RequestMapping(value = "getNotSendRequestByUserId", method = RequestMethod.POST)
    public ApiResponse<List<NewFriendRequestVo>> getNotSendRequestByUserId(@RequestBody Map<String, Object> params) {
        String userId = (String) params.get("userId");
        ApiResponse<List<NewFriendRequestVo>> apiResponse = new ApiResponse<>();
        List<NewFriendRequestVo> requestList = requestService.getNotSendRequestByUserId(userId);
        apiResponse.setMaxCount(requestList.size());
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("获取未接收消息列表成功");
        apiResponse.setData(requestList);
        return apiResponse;
    }

    /**
     * 根据userId获取其还未接收成功的好友请求消息的数量
     *
     * @return
     */
    @RequestMapping(value = "getNotSendRequestCountByUserId", method = RequestMethod.POST)
    public ApiResponse<Integer> getNotSendRequestCountByUserId(@RequestBody Map<String, Object> params) {
        String userId = (String) params.get("userId");
        ApiResponse<Integer> apiResponse = new ApiResponse<>();
        List<NewFriendRequestVo> requestList = requestService.getNotSendRequestByUserId(userId);
        apiResponse.setMaxCount(requestList.size());
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("获取未接收消息列表成功");
        apiResponse.setData(requestList.size());
        return apiResponse;
    }

    /**
     * 根据userId其接收到的好友请求消息
     *
     * @return
     */
    @RequestMapping(value = "getRequestByUserId", method = RequestMethod.POST)
    public ApiResponse<List<NewFriendRequestVo>> getRequestByUserId(@RequestBody Map<String, Object> params) {
        String userId = (String) params.get("userId");
        ApiResponse<List<NewFriendRequestVo>> apiResponse = new ApiResponse<>();
        List<NewFriendRequestVo> requestList = requestService.getRequestVoByUserId(userId);
        apiResponse.setMaxCount(requestList.size());
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("获取消息列表成功");
        apiResponse.setData(requestList);
        return apiResponse;
    }

}
