package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.Department;
import com.info.xiaotingtingBackEnd.model.DepartmentRelation;
import com.info.xiaotingtingBackEnd.model.User;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.repository.DepartmentRelationRep;
import com.info.xiaotingtingBackEnd.repository.DepartmentRep;
import com.info.xiaotingtingBackEnd.repository.UserRep;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("Department")
public class DepartmentController {

    @Autowired
    DepartmentRelationRep departmentRelationRep;
    @Autowired
    DepartmentRep departmentRep;
    @Autowired
    UserRep userRep;

    @RequestMapping(value = "addDepartment", method = RequestMethod.POST)
    public ApiResponse<Department> addDepartment(@RequestBody Department department) {
        ApiResponse<Department> apiResponse = new ApiResponse<Department>();
        if (departmentRep.findByDepartmentName(department.getDepartmentName()) != null) {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("部门已存在");
        } else {
            departmentRep.save(department);
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("添加部门成功");
            apiResponse.setData(department);
        }
        return apiResponse;
    }

    @RequestMapping(value = "getDepartments", method = RequestMethod.POST)
    public ApiResponse<List<Department>> getDepartments(@RequestBody Map<String, Integer> params) {
        int pageSize = params.get("pageSize");
        int pageNum = params.get("pageNum");
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setPageNum(pageNum);
        searchCondition.setSize(pageSize);
        ApiResponse<List<Department>> apiResponse = departmentRep.getPageBySearchCondition(searchCondition);
        return apiResponse;
    }

    @RequestMapping(value = "deleteDepartment", method = RequestMethod.POST)
    public ApiResponse<Object> deleteDepartment(@RequestBody String departmentId) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        if (departmentRep.findOne(departmentId) == null) {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("部门不存在");
        } else {
            departmentRep.delete(departmentId);
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("删除部门成功");
        }
        return apiResponse;
    }

    @RequestMapping(value = "addDepartmentRelation", method = RequestMethod.POST)
    public ApiResponse<Object> addDepartmentRelation(@RequestBody DepartmentRelation departmentRelation) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        if (userRep.findOne(departmentRelation.getUserId()) == null) {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("用户不存在");
        } else if (departmentRep.findOne(departmentRelation.getDepartmentId()) == null) {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("部门不存在");
        } else {
            departmentRelationRep.save(departmentRelation);
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("添加或修改用户所属部门关系成功");
        }
        return apiResponse;
    }

    @RequestMapping(value = "getUserByDepartment", method = RequestMethod.POST)
    public ApiResponse<List<User>> getUserByDepartment(@RequestBody Map<String, Object> params) {
        int pageSize = (int) params.get("pageSize");
        int pageNum = (int) params.get("pageNum");
        String departmentId = (String)params.get("departmentId");
        ApiResponse<List<User>> apiResponse = new ApiResponse<>();
        if (departmentRep.findOne(departmentId) == null) {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("部门不存在");
        } else {
            Pageable pageable = new PageRequest(pageNum - 1, pageSize);
            Page<User> userList = userRep.getUserByDepartment(departmentId, pageable);
            apiResponse.setCurrentPage(pageNum);
            apiResponse.setPageSize(pageSize);
            apiResponse.setMaxCount((int) userList.getTotalElements());
            apiResponse.setMaxPage(userList.getTotalPages());
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("获取部门成员成功");
            apiResponse.setData(userList.getContent());
        }
        return apiResponse;
    }
}
