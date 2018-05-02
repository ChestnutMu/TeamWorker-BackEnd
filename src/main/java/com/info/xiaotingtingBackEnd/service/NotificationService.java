package com.info.xiaotingtingBackEnd.service;

import com.google.gson.reflect.TypeToken;
import com.info.xiaotingtingBackEnd.constants.ChatConstants;
import com.info.xiaotingtingBackEnd.model.Chat;
import com.info.xiaotingtingBackEnd.model.ChatMessage;
import com.info.xiaotingtingBackEnd.model.Message;
import com.info.xiaotingtingBackEnd.model.Notification;
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
     * @param senderNickname
     * @param receiverIds
     * @param senderAvatar
     * @param title
     * @param content
     * @param photo
     * @throws PlatformException
     */
    public void sendNotification(String teamId, String senderId, String senderNickname, String senderAvatar, String receiverIds, String title, String content, String photo) throws PlatformException {
        Set<String> userList = gson.fromJson(receiverIds, new TypeToken<HashSet<String>>() {
        }.getType());
        if (userList.size() <= 0) {
            throw new PlatformException(-1, "发送通知没有接受者");
        }
        List<Notification> notificationList = new ArrayList<>(userList.size() - 1);
        Date now = new Date();
        for (String receiverId : userList) {
            if (receiverId.equals(senderId)) continue;
            Notification notification = new Notification();
            notification.setTeamId(teamId);
            notification.setSenderId(senderId);
            notification.setSenderNickname(senderNickname);
            notification.setSenderAvatar(senderAvatar);
            notification.setReceiverId(receiverId);
            notification.setTitle(title);
            notification.setContent(content);
            notification.setPhoto(photo);
            notification.setTime(now);
            notification.setSend(false);
            notificationList.add(notification);
        }
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

    @Override
    public NotificationRep getRepo() {
        return notificationRep;
    }

}
