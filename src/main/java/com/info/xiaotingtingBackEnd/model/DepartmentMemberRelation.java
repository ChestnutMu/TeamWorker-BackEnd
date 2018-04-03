package com.info.xiaotingtingBackEnd.model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/3 15:42:57
 * Description：部门成员关系
 * Email: xiaoting233zhang@126.com
 */

@Entity
@DynamicUpdate
@DynamicInsert
@IdClass(DepartmentMemberRelation.DepartmentMemberRelationId.class)
public class DepartmentMemberRelation implements Serializable {

    @Id
    private String departmentId;

    @Id
    private String userId;

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static class DepartmentMemberRelationId implements Serializable {

        public DepartmentMemberRelationId() {
        }

        public DepartmentMemberRelationId(String userId, String departmentId) {
            this.userId = userId;
            this.departmentId = departmentId;
        }

        private String userId;

        private String departmentId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(String departmentId) {
            this.departmentId = departmentId;
        }
    }
}
