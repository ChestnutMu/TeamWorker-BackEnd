package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.model.Message;
import com.info.xiaotingtingBackEnd.model.vo.MessageVo;
import com.info.xiaotingtingBackEnd.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
     * @param title
     * @param content
     * @param receiverIds
     * @return
     */
    public Message registerMessage(String userId, String title, String content, String receiverIds) {
        Message message = new Message();
        message.setSenderId(userId);
        message.setTitle(title);
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
     * @param title
     * @param content
     * @param uidList
     */
    public void sendMessage(String userId, String title, String content, List<String> uidList) {
        if (uidList.isEmpty()){
            System.out.println("发送消息没有接受者id");
            return;
        }
        for (String uid : uidList) {
            Message message = registerMessage(userId, title, content, uid);
            boolean isSuccess = handler.sendMessage(message);
            if (isSuccess) {
                hasSendMessage(message.getMessageId());
            }
        }
    }

    public Page<MessageVo> getMessagesByUserId(String userId, Pageable pageable){
        return messageRep.getMessagesByUserId(userId,pageable);
    }
}
