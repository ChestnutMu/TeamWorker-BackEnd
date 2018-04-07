package com.info.xiaotingtingBackEnd.repository;

import com.info.xiaotingtingBackEnd.model.User;
import com.info.xiaotingtingBackEnd.model.vo.UserVo;
import com.info.xiaotingtingBackEnd.pojo.DepartmentUser;
import com.info.xiaotingtingBackEnd.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/11 16:34:41
 * Description：User查询方法
 * Email: xiaoting233zhang@126.com
 */

@Repository
public interface UserRep extends BaseRepository<User, String> {

    User findByAccountAndPassword(String account, String password);

    User findByAccount(String account);

    @Query(value = "select new com.info.xiaotingtingBackEnd.pojo.DepartmentUser(u.userId,u.avatar,u.nickname)" +
            " from User u, DepartmentMemberRelation d " +
            "where d.departmentId = :departmentId " +
            "and u.userId = d.userId ",
            countQuery = "select count(d.userId) from DepartmentMemberRelation d" +
                    " where d.departmentId = :departmentId")
    Page<DepartmentUser> getUserByDepartment(@Param("departmentId") String departmentId, Pageable pageable);

    @Query(value = "select new com.info.xiaotingtingBackEnd.model.vo.UserVo" +
            "(u.userId,u.account,u.nickname,u.avatar,u.telephone,u.sex,u.birthday,u.region)" +
            " from User u, UserRelation ur" +
            " where (ur.userAId = :userId and u.userId = ur.userBId)" +
            " or (ur.userBId = :userId and u.userId = ur.userAId)",
            countQuery = "select count(ur.userAId) from UserRelation ur" +
                    "where ur.userAId = :userId or ur.userBId = :userId")
    List<UserVo> getMyFriend(@Param("userId") String userId);
}
