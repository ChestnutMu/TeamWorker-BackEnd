package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.Department;
import com.info.xiaotingtingBackEnd.model.DepartmentMemberRelation;
import com.info.xiaotingtingBackEnd.model.DepartmentRelation;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.repository.DepartmentRep;
import com.info.xiaotingtingBackEnd.service.base.BaseService;
import org.springframework.stereotype.Service;

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

    public ApiResponse addDepartmentMemberRelation(DepartmentMemberRelation departmentMemberRelation) {
        ApiResponse apiResponse = new ApiResponse<>();
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

    public ApiResponse addDepartmentMemberRelations(List<DepartmentMemberRelation> departmentMemberRelations) {
        ApiResponse apiResponse = new ApiResponse<>();
        departmentMemberRep.save(departmentMemberRelations);
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("添加或修改用户所属部门关系成功");
        return apiResponse;
    }

    public ApiResponse deleteDepartment(String departmentId) {
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
        ApiResponse apiResponse = new ApiResponse<>();
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

    public Department addDepartment(Department department) {
        if (departmentRep.findByDepartmentName(department.getDepartmentName()) != null) {
            return null;
        } else {
            return departmentRep.save(department);
        }
    }

    public ApiResponse addDepartmentRelation(DepartmentRelation departmentRelation) {
        ApiResponse apiResponse = new ApiResponse<>();
        if (departmentRep.findOne(departmentRelation.getMainDepartmentId()) != null
                && departmentRep.findOne(departmentRelation.getViceDepartmentId()) != null) {
            departmentRelationRep.save(departmentRelation);
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("添加部门关系成功");
        } else {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("无查询到部门记录");
        }
        return apiResponse;
    }

    public ApiResponse addDepartmentRelations(List<DepartmentRelation> departmentRelations) {
        ApiResponse<DepartmentRelation> apiResponse = new ApiResponse<>();
        departmentRelationRep.save(departmentRelations);
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("添加部门关系成功");
        return apiResponse;
    }

}
