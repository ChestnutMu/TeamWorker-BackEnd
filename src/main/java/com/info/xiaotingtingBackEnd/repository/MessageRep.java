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
            " us.account, u.receiverId, u.title, u.content, u.time, u.isRead)" +
            " from Message u,User us where u.receiverId = :userId and u.senderId = us.userId",
            countQuery = "select count(u.receiverId) from Message u" +
                    " where u.receiverId = :userId")
    Page<MessageVo> getMessagesByUserId(@Param("userId") String userId, Pageable pageable);
}
