package com.info.xiaotingtingBackEnd.service;

import com.google.gson.reflect.TypeToken;
import com.info.xiaotingtingBackEnd.constants.ChatConstants;
import com.info.xiaotingtingBackEnd.model.Chat;
import com.info.xiaotingtingBackEnd.model.ChatMessage;
import com.info.xiaotingtingBackEnd.model.vo.UserInfoVo;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.ChatRep;
import com.info.xiaotingtingBackEnd.repository.base.SearchBean;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.base.BaseService;
import com.info.xiaotingtingBackEnd.util.DataCheckUtil;
import org.springframework.stereotype.Service;

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
            Date now = new Date();
            if (temp != null) {//不用创建
                temp.setUpdateTime(now);
                chatRep.save(temp);
                return temp;
            }
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
        List<UserInfoVo> userInfoVoList = userRep.getUserInfo(userId);
        UserInfoVo userInfo = userInfoVoList.get(0);
        for (String receiverId : userList) {
            if (receiverId.equals(userId)) continue;
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setChatId(chatId);
            chatMessage.setSenderId(userId);
            chatMessage.setUserId(receiverId);
            chatMessage.setMessage(message);
            chatMessage.setSend(false);
            chatMessage.setSendTime(now);
            chatMessage.setNickname(userInfo.getNickname());
            chatMessage.setAvatar(userInfo.getAvatar());
            chatMessageList.add(chatMessage);
        }
        if (chat.getChatType().equals(ChatConstants.TYPE_CHAT_DOUBLE)) {
            chat.setLastMessage(message);
        } else {
            chat.setLastMessage(userInfo.getNickname() + ":" + message);
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
            if (newChat.getChatName().length() >= 15) {
                throw new PlatformException(-1, "已超过限定字数");
            }
            chat.setChatName(newChat.getChatName());
            chat.setUpdateTime(now);
//            chatRep.save(chat);
            sendChatMessageChangeInfo(userId, chat, ChatConstants.TYPE_MESSAGE_CHANGE_NAME, null);
        }
        if (!DataCheckUtil.isEmpty(newChat.getChatPic())) {
            chat.setChatPic(newChat.getChatPic());
            chat.setUpdateTime(now);
//            chatRep.save(chat);
            sendChatMessageChangeInfo(userId, chat, ChatConstants.TYPE_MESSAGE_CHANGE_PIC, null);
        }
        if (!DataCheckUtil.isEmpty(newChat.getUserList())) {
            Set<String> userList = gson.fromJson(newChat.getUserList(), new TypeToken<HashSet<String>>() {
            }.getType());
            List<String> userListOld = gson.fromJson(chat.getUserList(), new TypeToken<ArrayList<String>>() {
            }.getType());
            List<String> addUserList = new ArrayList<>();
            for (String temp : userList) {
                if (!userListOld.remove(temp)) {
                    addUserList.add(temp);
                }
            }
            //删除的userListOld 添加的addUserList

            chat.setUserList(gson.toJson(userList));
            chat.setUpdateTime(now);
//            chatRep.save(chat);
            if (userListOld.isEmpty() && !addUserList.isEmpty())
                sendChatMessageChangeInfo(userId, chat, ChatConstants.TYPE_MESSAGE_CHANGE_PEOPLE_ADD, addUserList);
            else if (!userListOld.isEmpty() && addUserList.isEmpty())
                sendChatMessageChangeInfo(userId, chat, ChatConstants.TYPE_MESSAGE_CHANGE_PEOPLE_REMOVE, userListOld);
        }
        return chat;
    }

    private void sendChatMessageChangeInfo(String userId, Chat chat, Integer type, List<String> userListOld) {
        Set<String> userList = gson.fromJson(chat.getUserList(), new TypeToken<HashSet<String>>() {
        }.getType());
        List<ChatMessage> chatMessageList = new ArrayList<>(userList.size() - 1);
        List<UserInfoVo> userInfoVoList = userRep.getUserInfo(userId);
        UserInfoVo userInfo = userInfoVoList.get(0);
        String message = "";
        List<UserInfoVo> userInfoList = null;
        if (type.equals(ChatConstants.TYPE_MESSAGE_CHANGE_NAME)) {
            message = chat.getChatName();
        } else if (type.equals(ChatConstants.TYPE_MESSAGE_CHANGE_PIC)) {
            message = chat.getChatPic();
        } else if (type.equals(ChatConstants.TYPE_MESSAGE_CHANGE_PEOPLE_ADD)) {
            userInfoList = userRep.getUserListInfo(userListOld);
            message = gson.toJson(userInfoList);
        } else if (type.equals(ChatConstants.TYPE_MESSAGE_CHANGE_PEOPLE_REMOVE)) {
            userInfoList = userRep.getUserListInfo(userListOld);
            message = gson.toJson(userInfoList);
        } else if (type.equals(ChatConstants.TYPE_MESSAGE_PEOPLE_OUT)) {
            Map<String, String> map = new HashMap<>();
            map.put("adminId", chat.getAdminId());
            map.put("userList", chat.getUserList());
            message = gson.toJson(map);
        }
        for (String receiverId : userList) {
            if (receiverId.equals(userId)) continue;
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setChatId(chat.getChatId());
            chatMessage.setSenderId(userId);
            chatMessage.setUserId(receiverId);
            chatMessage.setSend(false);
            chatMessage.setMessage(message);
            chatMessage.setType(type);
            chatMessage.setSendTime(chat.getUpdateTime());
            chatMessage.setNickname(userInfo.getNickname());
            chatMessage.setAvatar(userInfo.getAvatar());
            chatMessageList.add(chatMessage);
        }
        if (type.equals(ChatConstants.TYPE_MESSAGE_CHANGE_NAME)) {
            chat.setLastMessage(userInfo.getNickname() + "修改聊天室名称为“" + message + "”");
        } else if (type.equals(ChatConstants.TYPE_MESSAGE_CHANGE_PIC)) {
            chat.setLastMessage(userInfo.getNickname() + "修改聊天室头像");
        } else if (type.equals(ChatConstants.TYPE_MESSAGE_CHANGE_PEOPLE_ADD)) {
            String temp = "";
            for (int i = 0; i < userInfoList.size() - 1; i++) {
                temp = temp + userInfoList.get(i).getNickname() + "、";
            }
            temp = temp + userInfoList.get(userInfoList.size() - 1).getNickname();
            chat.setLastMessage(userInfo.getNickname() + "邀请" + temp + "加入了聊天室");
        } else if (type.equals(ChatConstants.TYPE_MESSAGE_CHANGE_PEOPLE_REMOVE)) {
            String temp = "";
            for (int i = 0; i < userInfoList.size() - 1; i++) {
                temp = temp + userInfoList.get(i).getNickname() + "、";
            }
            temp = temp + userInfoList.get(userInfoList.size() - 1).getNickname();
            chat.setLastMessage(userInfo.getNickname() + "将" + temp + "移出了聊天室");
        } else if (type.equals(ChatConstants.TYPE_MESSAGE_PEOPLE_OUT)) {
            chat.setLastMessage(userInfo.getNickname() + "退出了聊天室");
        }
        chatRep.save(chat);
        chatMessageRep.save(chatMessageList);
        handler.sendChatMessage(chatMessageList);
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

    public void goOutChat(String chatId, String userId) throws PlatformException {
        Chat chat = chatRep.findOne(chatId);
        if (!chat.getUserList().contains(userId))
            throw new PlatformException(-1, "你不在该聊天室");
        List<String> userListOld = gson.fromJson(chat.getUserList(), new TypeToken<ArrayList<String>>() {
        }.getType());
        userListOld.remove(userId);
        chat.setUpdateTime(new Date());
        if (userListOld.isEmpty()) {
            chatRep.delete(chatId);
            return;
        } else if (chat.getAdminId().equals(userId)) {
            chat.setAdminId(userListOld.get(0));
            chat.setUserList(gson.toJson(userListOld));
        }
        sendChatMessageChangeInfo(userId, chat, ChatConstants.TYPE_MESSAGE_PEOPLE_OUT, null);
    }
}
