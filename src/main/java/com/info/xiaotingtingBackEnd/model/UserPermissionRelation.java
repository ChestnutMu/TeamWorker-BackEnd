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
 * Description：用户角色
 * Email: xiaoting233zhang@126.com
 */

@Entity
@DynamicUpdate
@DynamicInsert
public class UserPermissionRelation implements Serializable {

    @Id
    @Column(columnDefinition = "char(20)", nullable = false)
    private String userId;

    private int permissionRange;//0标识整个公司; 1 所在部门及其子部门；2 标识特定部门

    private String permissonList; //json格式的Permisson列表

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPermissionRange() {
        return permissionRange;
    }

    public void setPermissionRange(int permissionRange) {
        this.permissionRange = permissionRange;
    }

    public String getPermissonList() {
        return permissonList;
    }

    public void setPermissonList(String permissonList) {
        this.permissonList = permissonList;
    }
}
