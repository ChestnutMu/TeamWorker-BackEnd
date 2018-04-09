package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.Permission;
import com.info.xiaotingtingBackEnd.model.UserPermissionRelation;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.service.NewFriendRequestService;
import com.info.xiaotingtingBackEnd.service.PermissionService;
import com.info.xiaotingtingBackEnd.service.UserPermissionRelationService;
import com.info.xiaotingtingBackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/8 12:16:20
 * Description：
 * Email: xiaoting233zhang@126.com
 */

@RestController
@RequestMapping("permission")
public class PermissionController {

    @Autowired
    UserPermissionRelationService permissionService;

    @RequestMapping(value = "addPermissionRelation", method = RequestMethod.POST)
    public ApiResponse<UserPermissionRelation> addPermissionRelation(@RequestBody UserPermissionRelation userPermissionRelation) {
        ApiResponse<UserPermissionRelation> apiResponse = new ApiResponse<>();
        UserPermissionRelation relation = permissionService.addPermissionRelation(userPermissionRelation);
        if (relation != null) {
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("添加权限成功");
            apiResponse.setData(relation);
        } else {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("添加权限失败");
        }
        return apiResponse;
    }


}
