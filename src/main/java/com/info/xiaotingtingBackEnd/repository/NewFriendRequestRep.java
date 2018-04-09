package com.info.xiaotingtingBackEnd.repository;

import com.info.xiaotingtingBackEnd.model.NewFriendRequest;
import com.info.xiaotingtingBackEnd.model.vo.NewFriendRequestVo;
import com.info.xiaotingtingBackEnd.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
            "(n.newFriendRequestId,n.requesterId,n.recipientId,n.authenticationMessage,n.time,n.isSend,n.isAccepted," +
            "u.telephone,u.nickname,u.avatar)" +
            " from NewFriendRequest n, User u" +
            " where n.recipientId = :userId and n.requesterId = u.userId and n.isSend=:isSend" +
            " order by n.time desc",
            countQuery = "select count(n) from NewFriendRequest n " +
                    "where n.recipientId = :userId")
    List<NewFriendRequestVo> getRequestVoByUserId(@Param("userId") String userId, @Param("isSend") boolean isSend);

    Long countAllByRecipientIdAndIsSend(String userId, boolean isSend);

    @Transactional
    @Modifying
    @Query(value = "update new_friend_request set is_send=:isSend where recipient_id=:userId and is_send=:oldSend", nativeQuery = true)
    void updateNewFriendRequestHadSend(@Param("userId") String userId, @Param("oldSend") boolean oldSend, @Param("isSend") boolean isSend);
}
