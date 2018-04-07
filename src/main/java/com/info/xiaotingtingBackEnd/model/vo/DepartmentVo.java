package com.info.xiaotingtingBackEnd.model.vo;

import com.info.xiaotingtingBackEnd.model.Department;
import com.info.xiaotingtingBackEnd.model.User;

import java.util.List;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/7 21:07:19
 * Description：
 * Email: xiaoting233zhang@126.com
 */
public class DepartmentVo {

    private Department department;

    private List<String> userIds;

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
