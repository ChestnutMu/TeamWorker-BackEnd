package com.info.xiaotingtingBackEnd.model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.util.Date;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/9 19:54:46
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Entity
@DynamicUpdate
@DynamicInsert
@IdClass(TeamRelation.TeamRelationId.class)
public class TeamRelation {

    @Id
    private String teamId;

    @Id
    private String userId;

    /*队员类型 TeamConstants*/
    private Integer type;

    private Date updateTime;

    public TeamRelation() {
    }

    public TeamRelation(String teamId, String userId) {
        this.teamId = teamId;
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static class TeamRelationId implements Serializable {

        @Id
        private String teamId;

        @Id
        private String userId;

        public TeamRelationId() {
        }

        public TeamRelationId(String teamId, String userId) {
            this.teamId = teamId;
            this.userId = userId;
        }

        public String getTeamId() {
            return teamId;
        }

        public void setTeamId(String teamId) {
            this.teamId = teamId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
