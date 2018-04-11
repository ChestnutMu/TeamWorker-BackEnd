package com.info.xiaotingtingBackEnd.service;

import com.google.gson.reflect.TypeToken;
import com.info.xiaotingtingBackEnd.constants.ChatConstants;
import com.info.xiaotingtingBackEnd.model.Chat;
import com.info.xiaotingtingBackEnd.model.ChatMessage;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.ChatRep;
import com.info.xiaotingtingBackEnd.repository.base.SearchBean;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.base.BaseService;
import com.info.xiaotingtingBackEnd.util.DataCheckUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/10 15:21:29
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Service
public class ChatService extends BaseService<Chat, String, ChatRep> {
    @Override
    public ChatRep getRepo() {
        return chatRep;
    }

    public Chat buildChat(Chat chat) throws PlatformException {
        if (DataCheckUtil.isEmpty(chat.getUserList()))
            throw new PlatformException(-1, "聊天室必须有人");
        if (chat.getChatType() == ChatConstants.TYPE_CHAT_DOUBLE) {
            Set<String> userList = gson.fromJson(chat.getUserList(), new TypeToken<HashSet<String>>() {
            }.getType());
            if (userList.size() != 2) {
                throw new PlatformException(-1, "双人聊天室必须两个人");
            }
            chat.setUserList(gson.toJson(userList));
            Chat temp = chatRep.findTopByUserListAndChatType(chat.getUserList(), ChatConstants.TYPE_CHAT_DOUBLE);
            if (temp != null) {//不用创建
                return temp;
            }
            Date now = new Date();
            chat.setCreateTime(now);
            chat.setUpdateTime(now);
            chatRep.save(chat);
        } else if (chat.getChatType() == ChatConstants.TYPE_CHAT_MULTIPLAYER) {
            if (DataCheckUtil.isEmpty(chat.getChatName()))
                throw new PlatformException(-1, "聊天室需要名字");
            Set<String> userList = gson.fromJson(chat.getUserList(), new TypeToken<HashSet<String>>() {
            }.getType());
            if (userList.size() <= 1) {
                throw new PlatformException(-1, "聊天室必须有人");
            }
            chat.setUserList(gson.toJson(userList));
            Date now = new Date();
            chat.setCreateTime(now);
            chat.setUpdateTime(now);
            chatRep.save(chat);
        } else {
            throw new PlatformException(-1, "需要说明聊天室类型");
        }
        return chat;
    }

    public void sendChatMessage(String chatId, String userId, String message) throws PlatformException {
        Chat chat = chatRep.findOne(chatId);
        if (chat == null)
            throw new PlatformException(-1, "聊天室不存在");
        if (!chat.getUserList().contains(userId))
            throw new PlatformException(1, "你已不在该群聊");
        Set<String> userList = gson.fromJson(chat.getUserList(), new TypeToken<HashSet<String>>() {
        }.getType());
        if (userList.size() <= 0) {
            throw new PlatformException(-1, "聊天室必须有人");
        }
        List<ChatMessage> chatMessageList = new ArrayList<>(userList.size() - 1);
        Date now = new Date();
        for (String receiverId : userList) {
            if (receiverId.equals(userId)) continue;
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setChatId(chatId);
            chatMessage.setSenderId(userId);
            chatMessage.setUserId(receiverId);
            chatMessage.setMessage(message);
            chatMessage.setSend(false);
            chatMessage.setSendTime(now);
            chatMessageList.add(chatMessage);
        }
        chat.setUpdateTime(now);
        chatRep.save(chat);
        chatMessageRep.save(chatMessageList);
        handler.sendChatMessage(chatMessageList);
    }

    public void hasSendChatMessage(String chatMessageId) {
        chatMessageRep.updateChatMessageHadSend(chatMessageId, true);
    }

    public Chat changeChatInfo(String userId, Chat newChat) throws PlatformException {
        Chat chat = chatRep.findOne(newChat.getChatId());
        if (chat == null)
            throw new PlatformException(-1, "聊天室不存在");
        if (chat.getChatType().equals(ChatConstants.TYPE_CHAT_DOUBLE))
            throw new PlatformException(-1, "双人聊天室不能修改信息");
        if (!chat.getAdminId().equals(userId))
            throw new PlatformException(-1, "你没有权限");
        Date now = new Date();
        if (!DataCheckUtil.isEmpty(newChat.getChatName())) {
            chat.setChatName(newChat.getChatName());
            chat.setUpdateTime(now);
        }
        if (!DataCheckUtil.isEmpty(newChat.getChatPic())) {
            chat.setChatPic(newChat.getChatPic());
            chat.setUpdateTime(now);
        }
        if (!DataCheckUtil.isEmpty(newChat.getUserList())) {
            Set<String> userList = gson.fromJson(newChat.getUserList(), new TypeToken<HashSet<String>>() {
            }.getType());
            chat.setUserList(gson.toJson(userList));
            chat.setUpdateTime(now);
        }
        return chatRep.save(chat);
    }

    public void releaseChat(String userId, String chatId) throws PlatformException {
        Chat chat = chatRep.findOne(chatId);
        if (chat == null)
            throw new PlatformException(-1, "聊天室不存在");
        if (chat.getChatType().equals(ChatConstants.TYPE_CHAT_DOUBLE))
            throw new PlatformException(-1, "双人聊天室不能解散");
        if (!chat.getAdminId().equals(userId))
            throw new PlatformException(-1, "你没有权限");
        chatRep.delete(chat);
    }

    /**
     * 返回未发送聊天室消息列表
     *
     * @param userId
     * @return
     */
    public List<ChatMessage> getChatMessageList(String userId) {
        List<ChatMessage> chatMessageList = chatMessageRep.findAllByUserIdAndSendOrderBySendTimeAsc(userId, false);
        return chatMessageList;
    }

    /**
     * 发送未接受信息
     */
    public void autoSendChatMessage() {
        List<ChatMessage> chatMessageList = chatMessageRep.findAllBySend(false);
        handler.sendChatMessage(chatMessageList);
    }

    public List<Chat> getChatList(String userId, String chatListJson) throws PlatformException {
        Set<String> chatList = gson.fromJson(chatListJson, new TypeToken<HashSet<String>>() {
        }.getType());
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.addSearchBean("chatId", chatList, SearchBean.OPERATOR_IN);
        List<Chat> chats = getListBySearchCondition(searchCondition);
        for (Chat chat : chats) {
            if (!chat.getUserList().contains(userId))
                throw new PlatformException(-1, "你不属于该聊天室");
        }
        return chats;
    }
}
