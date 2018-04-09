package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.Department;
import com.info.xiaotingtingBackEnd.model.DepartmentMemberRelation;
import com.info.xiaotingtingBackEnd.model.DepartmentRelation;
import com.info.xiaotingtingBackEnd.model.Team;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.repository.DepartmentRep;
import com.info.xiaotingtingBackEnd.repository.base.SearchBean;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
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

    public ApiResponse deleteDepartment(ApiResponse apiResponse, String departmentId) {
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

    public ApiResponse<List<Department>> getDepartmentsByTeamId(String teamId) {
        ApiResponse apiResponse = new ApiResponse<>();
        List<Department> departmentList = departmentRep.getDepartmentsByTeamId(teamId);
        if (departmentList.size() > 0) {
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("获取该团队下的所有部门成功");
            apiResponse.setData(departmentList);
        } else {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("该团队下无子部门");
        }
        return apiResponse;
    }

    public ApiResponse<List<Department>> getDepartmentsByDepartmentId(String departmentId) {
        ApiResponse apiResponse = new ApiResponse<>();
        List<Department> departmentList = departmentRep.getDepartmentsByDepartmentId(departmentId);
        if (departmentList.size() > 0) {
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("获取该部门下的所有部门成功");
            apiResponse.setData(departmentList);
        } else {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("该部门下无子部门");
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

    //递归算法，判断tartgetDepartment是否为mainDepartment的子部门或子部门的子部门
    public boolean isViceDepartment(String mainDepartmentId, String targetDepartmentId) {
        if (isMainViceDepartment(mainDepartmentId, targetDepartmentId)) {
            return true;
        } else {
            List<DepartmentRelation> viceDepartments = getViceDepartment(mainDepartmentId);
            if (viceDepartments != null) {
                for (DepartmentRelation departmentRelation : viceDepartments) {
                    isViceDepartment(departmentRelation.getViceDepartmentId(), targetDepartmentId);
                }
            } else {
                return false;
            }
        }
        return false;
    }

    //判断两个部门是否为主部门和子部门的关系
    public boolean isMainViceDepartment(String mainDepartmentId, String viceDepartment) {
        DepartmentRelation.DepartmentRelationId departmentRelation = new DepartmentRelation.DepartmentRelationId();
        departmentRelation.setMainDepartmentId(mainDepartmentId);
        departmentRelation.setViceDepartmentId(viceDepartment);
        if (departmentRelationRep.findOne(departmentRelation) != null) {
            return true;
        } else {
            return false;
        }
    }

    //获取一个部门的所有子部门
    public List<DepartmentRelation> getViceDepartment(String departmentId) {
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.addSearchBean("mainDepartmentId", departmentId, SearchBean.OPERATOR_EQ);
        return departmentRelationRep.getListBySearchCondition(searchCondition);
    }

    public Team addTeam(Team team) {
        if (teamRep.findByTeamName(team.getTeamName()) != null) {
            return null;
        } else {
            return teamRep.save(team);
        }
    }

    //判断两个部门是否属于同一个Team
    public boolean isBelongToSameTeam(String departmentId1, String departmentId2) {
        Department department1 = departmentRep.findOne(departmentId1);
        Department department2 = departmentRep.findOne(departmentId2);
        if(department1.getTeamId().equals(department2.getTeamId())){
            return true;
        }else {
            return false;
        }
    }
}
