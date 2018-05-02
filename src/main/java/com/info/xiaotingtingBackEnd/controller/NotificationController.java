package com.info.xiaotingtingBackEnd.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.Message;
import com.info.xiaotingtingBackEnd.model.TeamNotification;
import com.info.xiaotingtingBackEnd.model.TeamRelation;
import com.info.xiaotingtingBackEnd.model.vo.MessageVo;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.service.NotificationService;
import com.info.xiaotingtingBackEnd.util.DataCheckUtil;
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
 * Description：NotificationController
 * Email: xiaoting233zhang@126.com
 */
@RestController
@RequestMapping("notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    /**
     * 群发通知或聊天消息
     *
     * @return
     */
    @RequestMapping(value = "sendNotification", method = RequestMethod.POST)
    public ApiResponse<Object> sendNotification(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String teamId = params.get("teamId");
        String title = params.get("title");
        String content = params.get("content");
        String photo = params.get("photo");

        if (DataCheckUtil.isEmpty(title))
            throw new PlatformException(-1, "通知标题不能为空");
        if (DataCheckUtil.isEmpty(content))
            throw new PlatformException(-1, "通知内容不能为空");
        notificationService.sendNotification(teamId, userId, title, content, photo);
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        return apiResponse;

    }

    @RequestMapping(value = "getTeamNotificationList", method = RequestMethod.POST)
    public ApiResponse<List<TeamNotification>> getTeamNotificationList(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String teamId = params.get("teamId");
        List<TeamNotification> teamNotificationList = notificationService.getTeamNotificationList(teamId, userId);
        ApiResponse<List<TeamNotification>> apiResponse = new ApiResponse<>();
        apiResponse.setData(teamNotificationList);
        return apiResponse;
    }
}
