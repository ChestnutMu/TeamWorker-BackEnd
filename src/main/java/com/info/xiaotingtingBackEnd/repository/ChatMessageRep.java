package com.info.xiaotingtingBackEnd.repository;

import com.info.xiaotingtingBackEnd.model.ChatMessage;
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
 * CreateTime：at 2018/4/10 15:19:55
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Repository
public interface ChatMessageRep extends BaseRepository<ChatMessage, String> {


    @Transactional
    @Modifying
    @Query(value = "update chat_message set send=:send where chat_message_id=:chatMessageId", nativeQuery = true)
    void updateChatMessageHadSend(@Param("chatMessageId") String chatMessageId, @Param("send") boolean send);

    List<ChatMessage> findAllByUserIdAndSendOrderBySendTimeAsc(String userId, boolean send);

    List<ChatMessage> findAllBySend(boolean send);
}
