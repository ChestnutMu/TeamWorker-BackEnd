package com.info.xiaotingtingBackEnd.model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/3 15:45:26
 * Description：用户权限
 * Email: xiaoting233zhang@126.com
 */

@Entity
@DynamicUpdate
@DynamicInsert
public class UserPermissionRelation implements Serializable {

    @Id
    @Column(columnDefinition = "char(20)", nullable = false)
    private String userId;

    private String departmentId;//与权限范围相匹配，标识公司或所在部门或者特定部门的Id

    private int permissionRange;//0 标识整个团队；1 所在部门及其子部门；2 标识特定部门

    private String permissionId;

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

    public int getPermissionRange() {
        return permissionRange;
    }

    public void setPermissionRange(int permissionRange) {
        this.permissionRange = permissionRange;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }
}
