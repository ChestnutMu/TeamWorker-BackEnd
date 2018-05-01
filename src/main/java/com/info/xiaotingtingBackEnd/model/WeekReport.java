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
 * CreateTime：at 2018/5/1 11:59:18
 * Description：周报
 * Email: xiaoting233zhang@126.com
 */
@Entity
@DynamicUpdate
@DynamicInsert
public class WeekReport {
    @Id
    @GeneratedValue(generator = "attendanceIdGenerator")
    @GenericGenerator(name = "attendanceIdGenerator", strategy = "com.info.xiaotingtingBackEnd.model.base.IdGenerator")
    @Column(columnDefinition = "char(20)", nullable = false)
    private String weekReportId;

    /*用户id*/
    private String userId;

    /*用户昵称*/
    private String userNickname;

    /*用户头像*/
    private String userAvatar;

    /*团队id*/
    private String teamId;

    /*已完成工作*/
    private String finishedWork;

    /*工作总结*/
    private String workSummary;

    /*需协调工作*/
    private String needCoordinatedWork;

    /*图片*/
    private String photo;

    /*备注*/
    private String remarks;

    /*工作计划*/
    private String workPlan;

    /*提交时间*/
    private Date commitTime;

    public String getWeekReportId() {
        return weekReportId;
    }



    public void setWeekReportId(String weekReportId) {
        this.weekReportId = weekReportId;
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

    public String getFinishedWork() {
        return finishedWork;
    }

    public void setFinishedWork(String finishedWork) {
        this.finishedWork = finishedWork;
    }

    public String getWorkSummary() {
        return workSummary;
    }

    public void setWorkSummary(String workSummary) {
        this.workSummary = workSummary;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getWorkPlan() {
        return workPlan;
    }

    public void setWorkPlan(String workPlan) {
        this.workPlan = workPlan;
    }

    public String getNeedCoordinatedWork() {
        return needCoordinatedWork;
    }

    public void setNeedCoordinatedWork(String needCoordinatedWork) {
        this.needCoordinatedWork = needCoordinatedWork;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Date getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(Date commitTime) {
        this.commitTime = commitTime;
    }
}
