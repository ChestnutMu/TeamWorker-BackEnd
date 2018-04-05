package com.info.xiaotingtingBackEnd.model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/26 21:24:08
 * Description：部门之间的关系
 * Email: xiaoting233zhang@126.com
 */

@Entity
@DynamicUpdate
@DynamicInsert
@IdClass(DepartmentRelation.DepartmentRelationId.class)
public class DepartmentRelation implements Serializable {

    @Id
    private String mainDepartmentId;

    @Id
    private String viceDepartmentId;

    public String getMainDepartmentId() {
        return mainDepartmentId;
    }

    public void setMainDepartmentId(String mainDepartmentId) {
        this.mainDepartmentId = mainDepartmentId;
    }

    public String getViceDepartmentId() {
        return viceDepartmentId;
    }

    public void setViceDepartmentId(String viceDepartmentId) {
        this.viceDepartmentId = viceDepartmentId;
    }

    public static class DepartmentRelationId implements Serializable {

        private String mainDepartmentId;

        private String viceDepartmentId;

        public DepartmentRelationId() {
        }

        public DepartmentRelationId(String mainDepartmentId, String viceDepartmentId) {
            this.mainDepartmentId = mainDepartmentId;
            this.viceDepartmentId = viceDepartmentId;
        }

        public String getMainDepartmentId() {
            return mainDepartmentId;
        }

        public void setMainDepartmentId(String mainDepartmentId) {
            this.mainDepartmentId = mainDepartmentId;
        }

        public String getViceDepartmentId() {
            return viceDepartmentId;
        }

        public void setViceDepartmentId(String viceDepartmentId) {
            this.viceDepartmentId = viceDepartmentId;
        }
    }
}
