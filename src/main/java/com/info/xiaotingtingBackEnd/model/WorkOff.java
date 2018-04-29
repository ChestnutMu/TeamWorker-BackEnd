package com.info.xiaotingtingBackEnd.model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/3/28 21:30:08
 * Description：请假信息
 * Email: xiaoting233zhang@126.com
 */
@Entity
@DynamicUpdate
@DynamicInsert
public class WorkOff implements Serializable {
    @Id
    @GeneratedValue(generator = "attendanceIdGenerator")
    @GenericGenerator(name = "attendanceIdGenerator", strategy = "com.info.xiaotingtingBackEnd.model.base.IdGenerator")
    @Column(columnDefinition = "char(20)", nullable = false)
    private String workOffId;

    /*用户id*/
    private String userId;

    /*用户昵称*/
    private String userNickname;

    /*用户头像*/
    private String userAvatar;

    /*团队id*/
    private String teamId;

    /*请假类型*/
    private String workOffType;

    /*请假理由*/
    private String workOffReason;

    /*图片*/
    private String photo;

    /*开始时间*/
    private Date startTime;

    /*结束时间*/
    private Date endTime;

    /*提交时间*/
    private Date commitTime;

    /*处理id人*/
    private String adminId;

    /*处理人昵称*/
    private String adminNickname;

    /*处理人头像*/
    private String adminAvatar;


    /*处理信息*/
    private String handleReason;

    /*处理时间*/
    private Date handleTime;

    /*状态 WorkOffConstants*/
    private Integer status;

    public String getWorkOffId() {
        return workOffId;
    }

    public void setWorkOffId(String workOffId) {
        this.workOffId = workOffId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getWorkOffType() {
        return workOffType;
    }

    public void setWorkOffType(String workOffType) {
        this.workOffType = workOffType;
    }

    public String getWorkOffReason() {
        return workOffReason;
    }

    public void setWorkOffReason(String workOffReason) {
        this.workOffReason = workOffReason;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(Date commitTime) {
        this.commitTime = commitTime;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminNickname() {
        return adminNickname;
    }

    public void setAdminNickname(String adminNickname) {
        this.adminNickname = adminNickname;
    }

    public String getAdminAvatar() {
        return adminAvatar;
    }

    public void setAdminAvatar(String adminAvatar) {
        this.adminAvatar = adminAvatar;
    }

    public String getHandleReason() {
        return handleReason;
    }

    public void setHandleReason(String handleReason) {
        this.handleReason = handleReason;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
