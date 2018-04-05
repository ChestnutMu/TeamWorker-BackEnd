package com.info.xiaotingtingBackEnd.model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/26 21:19:02
 * Description：Department Bean
 * Email: xiaoting233zhang@126.com
 */

@Entity
@DynamicUpdate
@DynamicInsert
public class Department implements Serializable {
    @Id
    @GeneratedValue(generator = "userIdGenerator")
    @GenericGenerator(name = "userIdGenerator", strategy = "com.info.xiaotingtingBackEnd.model.base.IdGenerator")
    @Column(columnDefinition = "char(20)", nullable = false)
    private String departmentId;

    private String departmentName;

    private String departmentBadge;

    private String departmentIndustry;

    private String personnelScale;

    private String departmentRegion;

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentIndustry() {
        return departmentIndustry;
    }

    public void setDepartmentIndustry(String departmentIndustry) {
        this.departmentIndustry = departmentIndustry;
    }

    public String getDepartmentBadge() {
        return departmentBadge;
    }

    public void setDepartmentBadge(String departmentBadge) {
        this.departmentBadge = departmentBadge;
    }

    public String getDepartmentRegion() {
        return departmentRegion;
    }

    public void setDepartmentRegion(String departmentRegion) {
        this.departmentRegion = departmentRegion;
    }

    public String getPersonnelScale() {
        return personnelScale;
    }

    public void setPersonnelScale(String personnelScale) {
        this.personnelScale = personnelScale;
    }
}
