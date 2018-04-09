package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.model.NewFriendRequest;
import com.info.xiaotingtingBackEnd.model.vo.NewFriendRequestVo;
import com.info.xiaotingtingBackEnd.repository.NewFriendRequestRep;
import com.info.xiaotingtingBackEnd.repository.base.SearchBean;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/6 19:52:56
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Service
public class NewFriendRequestService extends BaseService<NewFriendRequest, String, NewFriendRequestRep> {

    /**
     * 保存消息
     *
     * @param newFriendRequest
     * @return
     */
    public NewFriendRequest registerRequest(NewFriendRequest newFriendRequest) {
        return newFriendRequestRep.save(newFriendRequest);

    }

    /**
     * 改变消息状态为（已发送）
     *
     * @param requestId
     * @return
     */
    public NewFriendRequest hasSendRequest(String requestId) {
        NewFriendRequest result = newFriendRequestRep.findOne(requestId);
        result.setSend(true);
        return newFriendRequestRep.save(result);
    }

    /**
     * 接受好友请求
     *
     * @param requestId
     * @return
     */
    public NewFriendRequest acceptRequest(String requestId) {
        NewFriendRequest result = newFriendRequestRep.findOne(requestId);
        result.setStatus(1);
        return newFriendRequestRep.save(result);
    }

    /**
     * 拒绝好友请求
     *
     * @param requestId
     * @return
     */
    public NewFriendRequest refuseRequest(String requestId) {
        NewFriendRequest result = newFriendRequestRep.findOne(requestId);
        result.setStatus(2);
        return newFriendRequestRep.save(result);
    }

    /**
     * 发送好友请求
     */
    public boolean sendNewFriendRequest(NewFriendRequest newFriendRequest) {
        NewFriendRequest request = registerRequest(newFriendRequest);
        handler.sendFriendRequest(request);
        if (handler.sendFriendRequest(request)) {
            hasSendRequest(request.getRequestId());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据userId获取其接收到的所有好友请求
     *
     * @param userId
     * @return
     */
    public List<NewFriendRequestVo> getRequestVoByUserId(String userId) {
        return newFriendRequestRep.getRequestVoByUserId(userId);
    }

    /**
     * 根据userId获取其还未接收到的好友请求消息
     *
     * @return
     */
    public List<NewFriendRequestVo> getNotSendRequestByUserId(String userId) {
        List<NewFriendRequestVo> result = newFriendRequestRep.getNotSendRequestVoByUserId(userId);
        return result;
    }

    @Override
    public NewFriendRequestRep getRepo() {
        return newFriendRequestRep;
    }
}
