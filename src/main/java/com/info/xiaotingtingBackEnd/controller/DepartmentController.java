package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.Department;
import com.info.xiaotingtingBackEnd.model.DepartmentMemberRelation;
import com.info.xiaotingtingBackEnd.model.DepartmentRelation;
import com.info.xiaotingtingBackEnd.model.vo.DepartmentVo;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.pojo.DepartmentUser;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.DepartmentService;
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
    public ApiResponse<Object> deleteDepartment(@RequestBody String departmentId) {
        return departmentService.deleteDepartment(departmentId);
    }

    @RequestMapping(value = "addDepartmentMemberRelation", method = RequestMethod.POST)
    public ApiResponse<DepartmentMemberRelation> addDepartmentMemberRelation(@RequestBody DepartmentMemberRelation departmentMemberRelation) {
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

    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "buildTeam", method = RequestMethod.POST)
    public ApiResponse<Department> buildTeam(@RequestBody DepartmentVo departmentVo) {
        ApiResponse<Department> apiResponse = new ApiResponse<>();
        Department department = departmentService.addDepartment(departmentVo.getDepartment());
        if (department != null) {
            List<DepartmentMemberRelation> relationList = new ArrayList<>(departmentVo.getUserIds().size());
            for(String userId:departmentVo.getUserIds()){
                relationList.add(new DepartmentMemberRelation(department.getDepartmentId(),userId));
            }
            departmentService.addDepartmentMemberRelations(relationList);
            apiResponse.setData(department);
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        } else {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("部门已存在");
        }
        return apiResponse;
    }
}
