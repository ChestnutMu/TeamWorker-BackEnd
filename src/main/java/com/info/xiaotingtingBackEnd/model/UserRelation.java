package com.info.xiaotingtingBackEnd.model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/6 11:07:46
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Entity
@DynamicUpdate
@DynamicInsert
@IdClass(UserRelation.UserRelationId.class)
public class UserRelation {

    @Id
    private String userAId;

    @Id
    private String userBId;

    public UserRelation() {
    }

    public UserRelation(String userAId, String userBId) {
        this.userAId = userAId;
        this.userBId = userBId;
    }

    public String getUserAId() {
        return userAId;
    }

    public void setUserAId(String userAId) {
        this.userAId = userAId;
    }

    public String getUserBId() {
        return userBId;
    }

    public void setUserBId(String userBId) {
        this.userBId = userBId;
    }

    public static class UserRelationId implements Serializable {

        private String userAId;

        private String userBId;

        public UserRelationId() {
        }

        public UserRelationId(String userAId, String userBId) {
            this.userAId = userAId;
            this.userBId = userBId;
        }

        public String getUserAId() {
            return userAId;
        }

        public void setUserAId(String userAId) {
            this.userAId = userAId;
        }

        public String getUserBId() {
            return userBId;
        }

        public void setUserBId(String userBId) {
            this.userBId = userBId;
        }
    }
}
