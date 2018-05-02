package com.info.xiaotingtingBackEnd.repository;

import com.info.xiaotingtingBackEnd.model.ChatMessage;
import com.info.xiaotingtingBackEnd.model.Message;
import com.info.xiaotingtingBackEnd.model.Notification;
import com.info.xiaotingtingBackEnd.model.vo.MessageVo;
import com.info.xiaotingtingBackEnd.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/31 09:19:23
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Repository
public interface NotificationRep extends BaseRepository<Notification, String> {

    @Transactional
    @Modifying
    @Query(value = "update notification set send=:send where notification_id=:notificationId", nativeQuery = true)
    void updateNotificationHadSend(@Param("notificationId") String notificationId, @Param("send") boolean send);

    List<Notification> findAllByReceiverIdAndSendOrderByTimeAsc(String receiver, boolean send);

}
