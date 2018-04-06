package com.info.xiaotingtingBackEnd.repository;

import com.info.xiaotingtingBackEnd.model.NewFriendRequest;
import com.info.xiaotingtingBackEnd.model.vo.NewFriendRequestVo;
import com.info.xiaotingtingBackEnd.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/6 19:50:09
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Repository
public interface NewFriendRequestRep extends BaseRepository<NewFriendRequest, String> {

    @Query(value = "select new com.info.xiaotingtingBackEnd.model.vo.NewFriendRequestVo" +
            "(n.requestId,n.requesterId,n.recipientId,n.authenticationMessage,n.time,n.isSend,n.isAccepted," +
            "u.account,u.nickname,u.avatar)" +
            " from NewFriendRequest n and User u" +
            " where n.recipientId = :userId and n.requesterId = u.userId" +
            " order by re.time desc",
            countQuery = "select count(d) from NewFriendRequest re" +
                    "where re.recipientId = :userId")
    List<NewFriendRequestVo> getRequestVoByUserId(@Param("userId") String userId);

    @Query(value = "select new com.info.xiaotingtingBackEnd.model.vo.NewFriendRequestVo" +
            "(n.requestId,n.requesterId,n.recipientId,n.authenticationMessage,n.time,n.isSend,n.isAccepted," +
            "u.account,u.nickname,u.avatar)" +
            " from NewFriendRequest re" +
            " where re.recipientId = :userId and re.recipientId.isSend = 0 and n.requesterId = u.userId",
            countQuery = "select count(d) from NewFriendRequest re" +
                    " where re.recipientId = :userId and re.recipientId.isSend = 0")
    List<NewFriendRequestVo> getNotSendRequestVoByUserId(@Param("userId") String userId);
}
