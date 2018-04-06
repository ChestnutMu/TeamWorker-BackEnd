package com.info.xiaotingtingBackEnd.model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/6 19:29:22
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Entity
@DynamicUpdate
@DynamicInsert
public class NewFriendRequest {

    @Id
    @GeneratedValue(generator = "requestId")
    @GenericGenerator(name = "requestId", strategy = "com.info.xiaotingtingBackEnd.model.base.IdGenerator")
    @Column(columnDefinition = "char(20)", nullable = false)
    private String requestId;

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

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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
