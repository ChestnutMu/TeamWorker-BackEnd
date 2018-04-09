package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.*;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.pojo.DepartmentUser;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.DepartmentService;
import com.info.xiaotingtingBackEnd.service.PermissionService;
import com.info.xiaotingtingBackEnd.service.UserPermissionRelationService;
import com.info.xiaotingtingBackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/11 16:21:47
 * Description：DepartmentController
 * Email: xiaoting233zhang@126.com
 */

@RestController
@RequestMapping("department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @Autowired
    UserService userService;

    @Autowired
    UserPermissionRelationService relationService;

    @Autowired
    PermissionService permissionService;

    String deleteDepartmentUri = "/department/deleteDepartment";

    String addDepartmentMemberRelationUri = "/department/addDepartmentMemberRelation";

    String addDepartmentMemberRelationsUri = "/department/addDepartmentMemberRelations";

    String getUserByDepartmentUri = "/department/getUserByDepartment";

    String addDepartmentRelationUri = "/department/addDepartmentRelation";

    String addDepartmentRelationsUri = "/department/addDepartmentsRelation";

    @RequestMapping(value = "getDepartments", method = RequestMethod.POST)
    public ApiResponse<List<Department>> getDepartments(@RequestBody Map<String, Integer> params) {
        int pageSize = params.get("pageSize");
        int pageNum = params.get("pageNum");
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setPageNum(pageNum);
        searchCondition.setSize(pageSize);
        return departmentService.getPageBySearchCondition(searchCondition);
    }

    @RequestMapping(value = "deleteDepartment", method = RequestMethod.POST)
    public ApiResponse deleteDepartment(@RequestHeader("uid") String userId, @RequestBody String departmentId) {
        ApiResponse apiResponse = new ApiResponse();
        if (permissionService.hasPermission(userId, departmentId, deleteDepartmentUri)) {
            return departmentService.deleteDepartment(apiResponse, departmentId);
        } else {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("你无删除该部门的权限");
            return apiResponse;
        }
    }

    @RequestMapping(value = "addDepartmentMemberRelation", method = RequestMethod.POST)
    public ApiResponse addDepartmentMemberRelation(@RequestHeader("uid") String userId, @RequestBody DepartmentMemberRelation departmentMemberRelation) {
        if (permissionService.hasPermission(userId, departmentMemberRelation.getDepartmentId(), addDepartmentRelationUri)) {
            return departmentService.addDepartmentMemberRelation(departmentMemberRelation);
        } else {
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("你对该部门没有添加部门成员的权限");
            return apiResponse;
        }
    }

    /**
     * @param userId
     * @param departmentMemberRelations
     * @return 为一个团队或部门添加多个成员
     */
    @RequestMapping(value = "addDepartmentMemberRelations", method = RequestMethod.POST)
    public ApiResponse<List<DepartmentMemberRelation>> addDepartmentMemberRelations(@RequestHeader("uid") String userId, @RequestBody List<DepartmentMemberRelation> departmentMemberRelations) {
        if (permissionService.hasPermission(userId, departmentMemberRelations.get(0).getDepartmentId(), addDepartmentRelationsUri)) {
            return departmentService.addDepartmentMemberRelations(departmentMemberRelations);
        } else {
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("你对该部门没有添加部门成员的权限");
            return apiResponse;
        }
    }

    @RequestMapping(value = "getUserByDepartment", method = RequestMethod.POST)
    public ApiResponse<List<DepartmentUser>> getUserByDepartment(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) {
        String departmentId = params.get("departmentId");
        if (permissionService.hasPermission(userId, departmentId, getUserByDepartmentUri)) {
            int pageNum = Integer.valueOf(params.get("pageNum"));
            int pageSize = Integer.valueOf(params.get("pageSize"));
            ApiResponse<List<DepartmentUser>> apiResponse = new ApiResponse<>();
            if (departmentService.findOne(departmentId) == null) {
                apiResponse.setStatus(HttpResponseCodes.FAILED);
                apiResponse.setMessage("部门不存在");
                return apiResponse;
            } else {
                return userService.getUserByDepartment(pageNum, pageSize, apiResponse, departmentId);
            }
        } else {
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("你没有获取该部门成员的权限");
            return apiResponse;
        }
    }

    @RequestMapping(value = "getDepartmentsByTeamId", method = RequestMethod.POST)
    public ApiResponse<List<Department>> getDepartmentsByTeamId(@RequestHeader("uid") String userId,@RequestBody Map<String,String> params) {
        return departmentService.getDepartmentsByTeamId(userId);
    }

    @RequestMapping(value = "getDepartmentsByDepartmentId", method = RequestMethod.POST)
    public ApiResponse<List<Department>> getDepartmentsByDepartmentId(@RequestHeader("uid") String userId,@RequestBody Map<String,String> params) {
        return departmentService.getDepartmentsByDepartmentId(userId);
    }

    @RequestMapping(value = "addDepartmentRelation", method = RequestMethod.POST)
    public ApiResponse<DepartmentRelation> addDepartmentRelation(@RequestBody DepartmentRelation departmentRelation) {
        return departmentService.addDepartmentRelation(departmentRelation);
    }

    @RequestMapping(value = "addDepartmentRelations", method = RequestMethod.POST)
    public ApiResponse<DepartmentRelation> addDepartmentRelations(@RequestBody List<DepartmentRelation> departmentRelations) {
        return departmentService.addDepartmentRelations(departmentRelations);
    }

}
