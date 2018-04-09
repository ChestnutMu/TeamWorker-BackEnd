package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.model.NewFriendRequest;
import com.info.xiaotingtingBackEnd.model.User;
import com.info.xiaotingtingBackEnd.model.UserRelation;
import com.info.xiaotingtingBackEnd.model.vo.NewFriendRequestVo;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.NewFriendRequestRep;
import com.info.xiaotingtingBackEnd.repository.base.SearchBean;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.base.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
     * 根据userId获取其接收到的所有好友请求
     *
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<NewFriendRequestVo> getRequestVoByUserId(String userId) {
        List<NewFriendRequestVo> requestVoList = newFriendRequestRep.getRequestVoByUserId(userId, false);
        //更新接收状态
        newFriendRequestRep.updateNewFriendRequestHadSend(userId, false, true);
        return requestVoList;
    }


    @Override
    public NewFriendRequestRep getRepo() {
        return newFriendRequestRep;
    }

    public void checkFriendRelation(String userAId, String userBId) throws PlatformException {
        //判断是否已是好友
        Long count = userRelationRep.countAllByUserAIdAndUserBId(userAId, userBId);
        if (count != null && count > 0)
            throw new PlatformException(-1, "已是好友关系");
        count = userRelationRep.countAllByUserAIdAndUserBId(userBId, userAId);
        if (count != null && count > 0)
            throw new PlatformException(-1, "已是好友关系");
    }

    public void sendFriendRequest(String userId, String recipientId, String message) throws PlatformException {
        if (message == null || "".equals(message))
            throw new PlatformException(-1, "必须要验证信息");
        //判断是否已是好友
        checkFriendRelation(userId, recipientId);

        NewFriendRequest request = new NewFriendRequest();
        request.setRequesterId(userId);
        request.setRecipientId(recipientId);
        request.setAuthenticationMessage(message);
        request.setTime(new Date());
        request.setSend(false);
        request.setAccepted(false);

        User user = userRep.findOne(recipientId);
        if (user == null)
            throw new PlatformException(-1, "用户不存在");

        newFriendRequestRep.save(request);

        handler.sendFriendRequest(request);
    }

    public long countNotSendRequestByUserId(String userId) {
        Long count = newFriendRequestRep.countAllByRecipientIdAndIsSend(userId, false);
        if (count == null)
            return 0;
        return count;
    }

    @Transactional(rollbackFor = Exception.class)
    public void acceptedRequest(String userId, String newFriendRequestId) throws PlatformException {
        NewFriendRequest newFriendRequest = newFriendRequestRep.findOne(newFriendRequestId);
        //判断是否已是好友
        checkFriendRelation(userId, newFriendRequest.getRequesterId());
        if (newFriendRequest == null)
            throw new PlatformException(-1, "好友请求不存在");
        if (!newFriendRequest.getRecipientId().equals(userId))
            throw new PlatformException(-1, "不能接受别人的好友请求");
        if (newFriendRequest.getAccepted().equals(true))
            throw new PlatformException(-1, "已接受该好友请求");
        newFriendRequest.setAccepted(true);
        newFriendRequestRep.save(newFriendRequest);
        UserRelation userRelation = new UserRelation();
        userRelation.setUserAId(userId);
        userRelation.setUserBId(newFriendRequest.getRequesterId());
        userRelationRep.save(userRelation);
    }
}
