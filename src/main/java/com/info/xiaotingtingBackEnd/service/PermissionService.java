package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.model.Permission;
import com.info.xiaotingtingBackEnd.repository.PermissionRep;
import com.info.xiaotingtingBackEnd.repository.base.SearchBean;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.base.BaseService;
import org.springframework.stereotype.Service;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/8 12:17:45
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Service
public class PermissionService extends BaseService<Permission, String, PermissionRep> {

    @Override
    public PermissionRep getRepo() {
        return permissionRep;
    }

}
