package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.model.Message;
import com.info.xiaotingtingBackEnd.model.vo.MessageVo;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.repository.base.SearchBean;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/31 09:49:02
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Service
public class MessageService extends BaseService {
    /**
     * 保存消息
     *
     * @param userId
     * @param content
     * @param receiverIds
     * @return
     */
    public Message registerMessage(String chatId, String chatName, String userId, String content, String receiverIds) {
        Message message = new Message();
        message.setChatId(chatId);
        message.setChatName(chatName);
        message.setSenderId(userId);
        message.setContent(content);
        message.setReceiverId(receiverIds);
        message.setTime(new Date());
        message.setSend(false);
        message.setRead(false);
        return messageRep.save(message);

    }


    /**
     * 改变消息状态为（已发送）
     *
     * @param messageId
     * @return
     */
    public Message hasSendMessage(String messageId) {
        Message result = messageRep.findOne(messageId);
        result.setSend(true);
        return messageRep.save(result);
    }

    /**
     * 改变消息状态为（已读）
     *
     * @param messageId
     * @return
     */
    public Message hasReadMessage(String messageId) {
        Message result = messageRep.findOne(messageId);
        result.setRead(true);
        return messageRep.save(result);
    }

    /**
     * 发送消息
     *
     * @param userId
     * @param content
     * @param uidList
     */
    public void sendMessage(String chatId, String chatName, String userId, String content, List<String> uidList) {
        if (uidList.isEmpty()) {
            System.out.println("发送消息没有接受者id");
            return;
        }
        for (String uid : uidList) {
            Message message = registerMessage(chatId, chatName, userId, content, uid);
            boolean isSuccess = handler.sendMessage(message);
            if (isSuccess) {
                hasSendMessage(message.getMessageId());
            }
        }
    }

    /**
     * 根据userId获取其接收到的所有消息
     *
     * @param userId
     * @param pageable
     * @return
     */
    public Page<MessageVo> getTopMessagesByUserId(String userId, Pageable pageable) {
        return messageRep.getTopMessagesByUserId(userId, pageable);
    }

    /**
     * 根据userId获取其还未接收到的消息
     *
     * @param userId
     * @return
     */
    public List<MessageVo> getNotSendMessageVosByUserId(String userId) {
        return messageRep.getNotSendMessageByUserId(userId);
    }


    /**
     * 根据userId获取其还未接收成功的消息
     *
     * @return
     */
    public List<Message> getNotSendMessagesByUserId(String userId) {
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.addSearchBean("receiverId", userId, SearchBean.OPERATOR_EQ);
        searchCondition.addSearchBean("isSend", false, SearchBean.OPERATOR_EQ);
        List<Message> result = messageRep.getListBySearchCondition(searchCondition);
        return result;
    }

}
