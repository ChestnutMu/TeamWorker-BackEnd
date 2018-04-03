package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.Attendance;
import com.info.xiaotingtingBackEnd.model.Department;
import com.info.xiaotingtingBackEnd.model.DepartmentMemberRelation;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.repository.AttendanceRep;
import com.info.xiaotingtingBackEnd.repository.DepartmentRelationRep;
import com.info.xiaotingtingBackEnd.repository.DepartmentRep;
import com.info.xiaotingtingBackEnd.repository.UserRep;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/3 16:27:29
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Service
public class DepartmentService extends BaseService<Department, String, DepartmentRep> {

    @Override
    public DepartmentRep getRepo() {
        return departmentRep;
    }

    public Department findByDepartmentName(String userId) {
        return departmentRep.findByDepartmentName(userId);
    }

    public ApiResponse<Object> addDepartmentRelation(DepartmentMemberRelation departmentMemberRelation) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        if (userRep.findOne(departmentMemberRelation.getUserId()) == null) {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("用户不存在");
        } else if (departmentRep.findOne(departmentMemberRelation.getDepartmentId()) == null) {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("部门不存在");
        } else {
            departmentMemberRep.save(departmentMemberRelation);
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("添加或修改用户所属部门关系成功");
        }
        return apiResponse;
    }

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

    public ApiResponse<List<Department>> getDepartmentByUserId(String userId) {
        ApiResponse<List<Department>> apiResponse = new ApiResponse<>();
        List<Department> departmentList = departmentRep.getDipartmentByUserId(userId);
        if (departmentList.size() > 0) {
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("获取用户所属部门成功");
            apiResponse.setData(departmentList);
        } else {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("您未加入任何部门");
        }
        return apiResponse;
    }

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
}
