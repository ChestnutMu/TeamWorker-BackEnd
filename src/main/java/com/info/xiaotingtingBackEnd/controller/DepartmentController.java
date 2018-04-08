package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.*;
import com.info.xiaotingtingBackEnd.model.vo.TeamVo;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.pojo.DepartmentUser;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.DepartmentService;
import com.info.xiaotingtingBackEnd.service.UserPermissionRelationService;
import com.info.xiaotingtingBackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        boolean hasPermission = false;
        List<UserPermissionRelation> permissionRelationList = relationService.getUserPermissionRelation(userId, deleteDepartmentUri);
        ApiResponse apiResponse = new ApiResponse();
        int permissionRange;
        if (permissionRelationList != null) {
            for (UserPermissionRelation relation : permissionRelationList) {
                if (departmentId.equals(relation.getDepartmentId())) {//如果获取到的用户权限列表中的存在请求删除的部门则可以删除该部门
                    hasPermission = true;
                } else {
                    permissionRange = relation.getPermissionRange();
                    if (permissionRange == 1) {//如果该权限范围为"所在部门及其子部门"，则查询该请求的departmentId是否为所在部门的子部门Id
                        if (departmentService.isViceDepartment(relation.getDepartmentId(), departmentId)) {
                            hasPermission = true;
                        }
                    }
                }
            }
        }
        if (hasPermission) {
            return departmentService.deleteDepartment(apiResponse, departmentId);
        }else {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("你无删除该部门的权限");
        }
        apiResponse.setStatus(HttpResponseCodes.FAILED);
        apiResponse.setMessage("删除部门失败");
        return apiResponse;
    }

    @RequestMapping(value = "addDepartmentMemberRelation", method = RequestMethod.POST)
    public ApiResponse addDepartmentMemberRelation(@RequestHeader("uid") String userId, @RequestBody DepartmentMemberRelation departmentMemberRelation) {
        if (relationService.getUserPermissionRelation(userId, addDepartmentRelationsUri) != null) {

        }
        return departmentService.addDepartmentMemberRelation(departmentMemberRelation);
    }

    @RequestMapping(value = "addDepartmentMemberRelations", method = RequestMethod.POST)
    public ApiResponse<List<DepartmentMemberRelation>> addDepartmentMemberRelations(@RequestBody List<DepartmentMemberRelation> departmentMemberRelations) {
        return departmentService.addDepartmentMemberRelations(departmentMemberRelations);
    }

    @RequestMapping(value = "getUserByDepartment", method = RequestMethod.POST)
    public ApiResponse<List<DepartmentUser>> getUserByDepartment(@RequestBody Map<String, String> params) {
        String departmentId = params.get("departmentId");
        int pageNum = Integer.valueOf(params.get("pageNum"));
        int pageSize = Integer.valueOf(params.get("pageSize"));
        ApiResponse<List<DepartmentUser>> apiResponse = new ApiResponse<>();
        if (departmentService.findOne(departmentId) == null) {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("部门不存在");
        } else {
            userService.getUserByDepartment(pageNum, pageSize, apiResponse, departmentId);
        }
        return apiResponse;
    }

    @RequestMapping(value = "getDepartmentByUserId", method = RequestMethod.POST)
    public ApiResponse<List<Department>> getDepartmentByUserId(@RequestHeader("uid") String userId) {
        return departmentService.getDepartmentByUserId(userId);
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
