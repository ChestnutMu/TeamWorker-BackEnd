package com.info.xiaotingtingBackEnd.model.vo;

import java.util.Date;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/6 21:42:11
 * Description：
 * Email: xiaoting233zhang@126.com
 */
public class NewFriendRequestVo {

    //好友请求Id
    private String newFriendRequestId;

    //好友请求者Id
    private String requesterId;

    //好友请求接收者Id
    private String recipientId;

    //验证消息
    private String authenticationMessage;

    //好友请求时间
    private Date time;

    //已发送
    private Boolean isSend;

    //已接受好友请求
    private Boolean isAccepted;

    //好友请求者账号
    private String requesterTelephone;

    //好友请求者昵称
    private String requesterNickname;

    //好友请求者头像
    private String requesterAvatar;

    public NewFriendRequestVo(String newFriendRequestId, String requesterId, String recipientId, String authenticationMessage, Date time, Boolean isSend, Boolean isAccepted, String requesterTelephone, String requesterNickname, String requesterAvatar) {
        this.newFriendRequestId = newFriendRequestId;
        this.requesterId = requesterId;
        this.recipientId = recipientId;
        this.authenticationMessage = authenticationMessage;
        this.time = time;
        this.isSend = isSend;
        this.isAccepted = isAccepted;
        this.requesterTelephone = requesterTelephone;
        this.requesterNickname = requesterNickname;
        this.requesterAvatar = requesterAvatar;
    }

    public String getNewFriendRequestId() {
        return newFriendRequestId;
    }

    public void setNewFriendRequestId(String newFriendRequestId) {
        this.newFriendRequestId = newFriendRequestId;
    }

    public String getRequesterAvatar() {
        return requesterAvatar;
    }

    public void setRequesterAvatar(String requesterAvatar) {
        this.requesterAvatar = requesterAvatar;
    }

    public String getRequesterTelephone() {
        return requesterTelephone;
    }

    public void setRequesterTelephone(String requesterTelephone) {
        this.requesterTelephone = requesterTelephone;
    }

    public String getRequesterNickname() {
        return requesterNickname;
    }

    public void setRequesterNickname(String requesterNickname) {
        this.requesterNickname = requesterNickname;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getAuthenticationMessage() {
        return authenticationMessage;
    }

    public void setAuthenticationMessage(String authenticationMessage) {
        this.authenticationMessage = authenticationMessage;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Boolean getSend() {
        return isSend;
    }

    public void setSend(Boolean send) {
        isSend = send;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }
}
