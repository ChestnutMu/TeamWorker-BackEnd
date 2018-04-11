package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.User;
import com.info.xiaotingtingBackEnd.model.UserRelation;
import com.info.xiaotingtingBackEnd.model.vo.FriendUserVo;
import com.info.xiaotingtingBackEnd.model.vo.UserVo;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.pojo.DepartmentUser;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.UserRep;
import com.info.xiaotingtingBackEnd.repository.base.SearchBean;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.base.BaseService;
import com.info.xiaotingtingBackEnd.util.DataCheckUtil;
import com.info.xiaotingtingBackEnd.util.EntityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/1 14:23:58
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Service
public class UserService extends BaseService<User, String, UserRep> {

    public boolean idAuth(String uid, String token) {
        User user = userRep.findOne(uid);
        if (null == user) {
            return false;
        }
        if (user.getToken().equals(token)) {
            return true;
        }
        return false;
    }

    public boolean addUserRelation(String userAId, String userBId) {
        UserRelation userRelation = new UserRelation();
        userRelation.setUserBId(userBId);
        userRelation.setUserAId(userAId);
        if (userRelationRep.save(userRelation) != null) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteUserRelation(String userAId, String userBId) {
        UserRelation userRelation = new UserRelation(userAId, userBId);
        userRelationRep.delete(userRelation);
        userRelation.setUserAId(userBId);
        userRelation.setUserBId(userAId);
        userRelationRep.delete(userRelation);
    }

    public ApiResponse<List<DepartmentUser>> getUserByDepartment(int pageNum, int pageSize, ApiResponse<List<DepartmentUser>> apiResponse, String departmentId) {
        Pageable pageable = new PageRequest(pageNum - 1, pageSize);
        Page<DepartmentUser> userList = userRep.getUserByDepartment(departmentId, pageable);
        apiResponse.setCurrentPage(pageNum);
        apiResponse.setPageSize(pageSize);
        apiResponse.setMaxCount((int) userList.getTotalElements());
        apiResponse.setMaxPage(userList.getTotalPages());
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("获取部门成员成功");
        apiResponse.setData(userList.getContent());
        return apiResponse;
    }

    public boolean isMyFriend(String myUserId, String userId) {
        UserRelation.UserRelationId userRelation = new UserRelation.UserRelationId(myUserId, userId);
        if (userRelationRep.findOne(userRelation) != null) {
            return true;
        } else {
            return false;
        }
    }

    public ApiResponse<List<UserVo>> getMyFriends(String userId, int pageNum, int pageSize) {
        Pageable pageable = new PageRequest(pageNum - 1, pageSize);
        Page<UserVo> result = userRep.getMyFriend(userId, pageable);

        ApiResponse<List<UserVo>> apiResponse = new ApiResponse<>();
        apiResponse.setCurrentPage(pageNum);
        apiResponse.setMaxPage(result.getTotalPages());
        apiResponse.setMaxCount((int) result.getTotalElements());
        apiResponse.setPageSize(result.getSize());
        apiResponse.setData(result.getContent());
        return apiResponse;
    }

    @Override
    public UserRep getRepo() {
        return userRep;
    }

    public User register(String telephone, String verificationCode, String password) throws Exception {
        if (null == verificationCode || !"0000".equals(verificationCode))
            throw new PlatformException(-1, "验证码不正确");
        if (userRep.findByTelephone(telephone) != null)
            throw new PlatformException(-1, "该手机号码已经被注册了");
        if (DataCheckUtil.isEmpty(password))
            throw new PlatformException(-1, "密码不能为空");
        User user = new User();
        user.setTelephone(telephone);
        user.setNickname(telephone);
        user.setSex("男");
        user.setPassword(password);
        user = userRep.save(user);
        user.setPassword(null);
        return user;
    }

    public User login(String telephone, String password) throws Exception {
        User user = userRep.findByTelephone(telephone);
        if (user == null)
            throw new PlatformException(-1, "该用户不存在");
        if (!user.getPassword().equals(password))
            throw new PlatformException(-1, "密码不正确");
        //若有其他设备登陆则踢下线
        handler.offline(user.getUserId(), user.getToken());
        user.setToken(EntityUtil.getIdByTimeStampAndRandom());
        user = userRep.save(user);
        user.setPassword(null);
        return user;
    }

    public User rememberMe(String userId) throws PlatformException {
        User user = userRep.findOne(userId);
        if (user == null)
            throw new PlatformException(-1, "该用户不存在");
        user.setToken(EntityUtil.getIdByTimeStampAndRandom());
        user = userRep.save(user);
        user.setPassword(null);
        return user;
    }

    public List<User> searchUser(String keyword) {
        List<User> users = userRep.searchUser("%" + keyword + "%");
        for (User user : users) {
            user.setPassword(null);
        }
        return users;
    }

    public boolean isFriend(String userId, String friendId) {
        //判断是否已是好友
        Long count = userRelationRep.countAllByUserAIdAndUserBId(userId, friendId);
        if (count != null && count > 0)
            return true;
        count = userRelationRep.countAllByUserAIdAndUserBId(friendId, userId);
        if (count != null && count > 0)
            return true;
        return false;
    }

    public FriendUserVo getUserDetail(String userId, String friendId) {
        User user = userRep.findOne(friendId);
        user.setPassword(null);
        user.setToken(null);

        //判断是否已是好友
        FriendUserVo friendUserVo = new FriendUserVo();
        friendUserVo.setUser(user);
        friendUserVo.setFriend(isMyFriend(userId, friendId));
        return friendUserVo;
    }
}
