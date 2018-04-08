package com.info.xiaotingtingBackEnd.repository;

import com.info.xiaotingtingBackEnd.model.UserPermissionRelation;
import com.info.xiaotingtingBackEnd.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/8 12:18:09
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Repository
public interface UserPermissionRelationRep extends BaseRepository<UserPermissionRelation, String> {


    @Query(value = "select u from UserPermissionRelation u,Permission p" +
            " where u.userId = :userId" +
            " and u.permissionId = p.permissionId and p.permissionUri = :permissionUri",
            countQuery = "select count(u.userId) from UserPermissionRelation u,Permission p"+
            " where u.userId = :userId"+
            " and u.permissionId = p.permissionId and p.permissionUri = :permissionUri")
    List<UserPermissionRelation> getUserPermissionRelation(@Param("userId") String userId,
                                                           @Param("permissionUri") String permissionUri);
}
