package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.model.Department;
import com.info.xiaotingtingBackEnd.model.DepartmentRelation;
import com.info.xiaotingtingBackEnd.model.Permission;
import com.info.xiaotingtingBackEnd.model.UserPermissionRelation;
import com.info.xiaotingtingBackEnd.repository.PermissionRep;
import com.info.xiaotingtingBackEnd.repository.base.SearchBean;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/8 12:17:45
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Service
public class PermissionService extends BaseService<Permission, String, PermissionRep> {

    @Override
    public PermissionRep getRepo() {
        return permissionRep;
    }

    /**
     * @param userId
     * @param departmentId
     * @param permissionUri
     * @return 判断用户对于请求的部门有没有该Uri的权限
     */
    public boolean hasPermission(String userId, String departmentId,String permissionUri) {
        List<UserPermissionRelation> permissionRelationList = userPermissionRelationRep.getUserPermissionRelation(userId, permissionUri);
        boolean hasPermission = false;
        if (permissionRelationList != null) {
            hasPermission = false;
            for (UserPermissionRelation relation : permissionRelationList) {
                if (departmentId.equals(relation.getDepartmentId())) {//如果获取到的用户权限列表中的存在请求删除的部门则可以删除该部门
                    hasPermission = true;
                } else {
                    int permissionRange = relation.getPermissionRange();
                    if (permissionRange == 0) {//如果权限范围为"整个团队",则查询要删除的部门与权限中的部门是否属于同一个团队
                        if (isBelongToSameTeam(relation.getDepartmentId(), departmentId)) {
                            hasPermission = true;
                        }
                    } else if (permissionRange == 1) {//如果该权限范围为"所在部门及其子部门"，则查询该请求的departmentId是否为所在部门的子部门Id
                        if (isViceDepartment(relation.getDepartmentId(), departmentId)) {
                            hasPermission = true;
                        }
                    }
                }
            }
        }
        return hasPermission;
    }

    //判断两个部门是否属于同一个Team
    public boolean isBelongToSameTeam(String departmentId1, String departmentId2) {
        Department department1 = departmentRep.findOne(departmentId1);
        Department department2 = departmentRep.findOne(departmentId2);
        if (department1.getTeamId().equals(department2.getTeamId())) {
            return true;
        } else {
            return false;
        }
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
}
