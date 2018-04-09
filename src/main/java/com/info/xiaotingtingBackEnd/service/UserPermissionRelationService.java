package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.model.Permission;
import com.info.xiaotingtingBackEnd.model.UserPermissionRelation;
import com.info.xiaotingtingBackEnd.repository.PermissionRep;
import com.info.xiaotingtingBackEnd.repository.UserPermissionRelationRep;
import com.info.xiaotingtingBackEnd.repository.base.SearchBean;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/8 12:17:45
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Service
public class UserPermissionRelationService extends BaseService<UserPermissionRelation, String, UserPermissionRelationRep> {

    @Override
    public UserPermissionRelationRep getRepo() {
        return userPermissionRelationRep;
    }

    /**
     *
     * @param userId
     * @param permissionUri
     * @return 获取该用户对于该接口的所有请求权限
     */
    public List<UserPermissionRelation> getUserPermissionRelation(String userId, String permissionUri) {
        return userPermissionRelationRep.getUserPermissionRelation(userId, permissionUri);
    }

    /**
     *
     * @param userPermissionRelation
     * @return 给用户添加权限
     */
    public UserPermissionRelation addPermissionRelation(UserPermissionRelation userPermissionRelation) {
        return userPermissionRelationRep.save(userPermissionRelation);
    }

}