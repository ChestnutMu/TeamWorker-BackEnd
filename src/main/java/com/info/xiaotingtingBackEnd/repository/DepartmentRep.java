package com.info.xiaotingtingBackEnd.repository;

import com.info.xiaotingtingBackEnd.model.Department;
import com.info.xiaotingtingBackEnd.model.vo.MessageVo;
import com.info.xiaotingtingBackEnd.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/26 21:26:23
 * Description：
 * Email: xiaoting233zhang@126.com
 */

@Repository
public interface DepartmentRep extends BaseRepository<Department, String> {

    Department findByDepartmentName(String name);

    @Query(value = "select d from DepartmentRelation dr,Department d " +
            "where dr.userId = :userId and dr.departmentId = d.departmentId",
            countQuery = "select count(d) from DepartmentRelation dr,Department d " +
                    "where dr.userId = :userId and dr.departmentId = d.departmentId")
    List<Department> getDipartmentByUserId(@Param("userId") String userId);
}
