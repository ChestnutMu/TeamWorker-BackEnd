package com.info.xiaotingtingBackEnd.repository;

import com.info.xiaotingtingBackEnd.model.Message;
import com.info.xiaotingtingBackEnd.model.User;
import com.info.xiaotingtingBackEnd.model.vo.MessageVo;
import com.info.xiaotingtingBackEnd.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/31 09:19:23
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Repository
public interface MessageRep extends BaseRepository<Message, String> {

    @Query(value = "select new com.info.xiaotingtingBackEnd.model.vo.MessageVo(u.messageId,u.senderId," +
            " us.nickname, u.receiverId, u.content, u.time, u.isRead)" +
            " from Message u,User us where u.receiverId = :userId and u.senderId = us.userId" +
            " order by u.time desc",
            countQuery = "select count(u.receiverId) from Message u" +
                    " where u.receiverId = :userId")
    Page<MessageVo> getMessagesByUserId(@Param("userId") String userId, Pageable pageable);

    @Query(value = "select new com.info.xiaotingtingBackEnd.model.vo.MessageVo(u.messageId,u.senderId," +
            " us.nickname, u.receiverId, u.content, u.time, u.isRead)" +
            " from Message u,User us where u.receiverId = :userId and u.senderId = us.userId" +
            " group by u.senderId order by u.time asc",
            countQuery = "select count(u.receiverId) from Message u" +
                    " where u.receiverId = :userId group by u.senderId")
    Page<MessageVo> getTopMessagesByUserId(@Param("userId") String userId, Pageable pageable);

    @Query(value = "select new com.info.xiaotingtingBackEnd.model.vo.MessageVo(m.messageId,m.senderId," +
            " u.nickname, m.receiverId, m.content, m.time, m.isRead)" +
            " from Message m,User u where m.receiverId = :userId and m.isSend = 0 and u.userId = m.senderId",
            countQuery = "select count(m.receiverId) from Message m" +
                    " where m.receiverId = :userId and m.isSend = 0")
    List<MessageVo> getNotSendMessageByUserId(@Param("userId") String userId);
}
