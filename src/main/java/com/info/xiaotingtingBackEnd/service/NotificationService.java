package com.info.xiaotingtingBackEnd.service;

import com.google.gson.reflect.TypeToken;
import com.info.xiaotingtingBackEnd.constants.ChatConstants;
import com.info.xiaotingtingBackEnd.constants.TeamConstants;
import com.info.xiaotingtingBackEnd.model.*;
import com.info.xiaotingtingBackEnd.model.vo.MessageVo;
import com.info.xiaotingtingBackEnd.model.vo.UserInfoVo;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.MessageRep;
import com.info.xiaotingtingBackEnd.repository.NotificationRep;
import com.info.xiaotingtingBackEnd.repository.base.SearchBean;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.base.BaseService;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/31 09:49:02
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Service
public class NotificationService extends BaseService<Notification, String, NotificationRep> {

    /**
     * 发送通知
     *
     * @param teamId
     * @param senderId
     * @param title
     * @param content
     * @param photo
     * @throws PlatformException
     */
    public void sendNotification(String teamId, String senderId, String title, String content, String photo) throws PlatformException {
        TeamRelation teamRelation = teamRelationRep.findOne(new TeamRelation.TeamRelationId(teamId, senderId));
        if (null == teamRelation)
            throw new PlatformException(-1, "你不属于该团队");
        if (!teamRelation.getType().equals(TeamConstants.TYPE_TEAM_ADMIN) && !teamRelation.getType().equals(TeamConstants.TYPE_TEAM_OWNER))
            throw new PlatformException(-1, "没有发通知权限");
        List<String> userList = teamRelationRep.getUserIdListByTeamId(teamId);

        List<UserInfoVo> userInfoVoList = userRep.getUserInfo(senderId);
        UserInfoVo userInfoVo = userInfoVoList.get(0);
        List<Notification> notificationList = new ArrayList<>(userList.size() - 1);
        Date now = new Date();
        for (String receiverId : userList) {
            if (receiverId.equals(senderId)) continue;
            Notification notification = new Notification();
            notification.setTeamId(teamId);
            notification.setSenderId(senderId);
            notification.setSenderNickname(userInfoVo.getNickname());
            notification.setSenderAvatar(userInfoVo.getAvatar());
            notification.setReceiverId(receiverId);
            notification.setTitle(title);
            notification.setContent(content);
            notification.setPhoto(photo);
            notification.setTime(now);
            notification.setSend(false);
            notificationList.add(notification);
        }
        //保存团队公告
        TeamNotification teamNotification = new TeamNotification();
        teamNotification.setTeamId(teamId);
        teamNotification.setSenderNickname(userInfoVo.getNickname());
        teamNotification.setSenderAvatar(userInfoVo.getAvatar());
        teamNotification.setTitle(title);
        teamNotification.setContent(content);
        teamNotification.setPhoto(photo);
        teamNotification.setTime(now);
        teamNotificationRep.save(teamNotification);
        //保存团队中所有人接收到的公告
        notificationRep.save(notificationList);
        handler.sendNotification(notificationList);
    }

    public void hasSendNotification(String notificationId) {
        notificationRep.updateNotificationHadSend(notificationId, true);
    }

    /**
     * 返回未发送通知列表
     *
     * @param userId
     * @return
     */
    public List<Notification> getNotificationList(String userId) {
        List<Notification> notificationList = notificationRep.findAllByReceiverIdAndSendOrderByTimeAsc(userId, false);
        return notificationList;
    }

    /**
     * 获取团队中的所有公告
     *
     * @param teamId
     * @return
     */
    public List<TeamNotification> getTeamNotificationList(String teamId, String userId) throws PlatformException {
        TeamRelation teamRelation = teamRelationRep.findOne(new TeamRelation.TeamRelationId(teamId, userId));
        if (null == teamRelation)
            throw new PlatformException(-1, "你不属于该团队");
        List<TeamNotification> teamNotificationList = teamNotificationRep.findAllByTeamIdOrderByTimeDesc(teamId);
        return teamNotificationList;
    }

    @Override
    public NotificationRep getRepo() {
        return notificationRep;
    }

}
