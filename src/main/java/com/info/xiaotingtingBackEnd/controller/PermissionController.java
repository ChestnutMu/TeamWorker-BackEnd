package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.Permission;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.service.NewFriendRequestService;
import com.info.xiaotingtingBackEnd.service.PermissionService;
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
    PermissionService permissionService;

    @RequestMapping(value = "addPermission", method = RequestMethod.POST)
    public ApiResponse<Permission> addPermission(@RequestBody Permission permission) {

        ApiResponse<Permission> apiResponse = new ApiResponse<>();
        Permission newPermission = permissionService.save(permission);
        if (newPermission != null) {
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("添加权限成功");
            apiResponse.setData(newPermission);
        }else {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("添加权限失败");
        }
        return apiResponse;
    }
}
