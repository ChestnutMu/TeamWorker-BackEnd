package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.Department;
import com.info.xiaotingtingBackEnd.model.User;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.repository.UserRep;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.socket.SenderEventHandler;
import com.info.xiaotingtingBackEnd.uitl.EntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/11 16:21:47
 * Description：UserController
 * Email: xiaoting233zhang@126.com
 */

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserRep userRep;

    @Autowired
    SenderEventHandler handler;


    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String test(int msgId, String message) {


        Map<String, Object> data = new HashMap<String, Object>();
        data.put("uid", "123456");
        data.put("message", message);

        handler.sendMessageToUser(msgId, data);
        return "123";
    }

    @RequestMapping(value = "addUser", method = RequestMethod.POST)
    public ApiResponse<User> addUser(User user) {
        ApiResponse<User> apiResponse = new ApiResponse<User>();
        if (userRep.findByAccount(user.getAccount()) != null) {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("账户已存在");
        } else {
            userRep.save(user);
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("注册成功");
            apiResponse.setData(user);
        }
        return apiResponse;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ApiResponse login(String account, String password) {
        ApiResponse<User> apiResponse = new ApiResponse<User>();
        System.out.println(account + ":" + password);
        User user = userRep.findByAccountAndPassword(account, password);
        if (user != null) {
            System.out.println(user + " " + account + ":" + password);
            user.setToken(EntityUtil.getIdByTimeStampAndRandom());
            user = userRep.save(user);
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("登陆成功");
            apiResponse.setData(user);
        } else {
            System.out.println(user + " " + account + ":" + password);
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("用户名或密码错误");
            apiResponse.setData(new User());
        }
        return apiResponse;
    }

    @RequestMapping(value = "rememberMe", method = RequestMethod.POST)
    public ApiResponse rememberMe(String userId, String token) {
        ApiResponse<User> apiResponse = new ApiResponse<User>();
        System.out.println(userId + ":" + token);
        User user = userRep.findOne(userId);
        if (user != null && user.getToken().equals(token)) {
            System.out.println(user + " " + userId + ":" + token);
            user.setToken(EntityUtil.getIdByTimeStampAndRandom());
            user = userRep.save(user);
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("登陆成功");
            apiResponse.setData(user);
        } else {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("用户名或密码错误");
            apiResponse.setData(new User());
        }
        return apiResponse;
    }

    @RequestMapping(value = "getAllUsers", method = RequestMethod.POST)
    public ApiResponse<List<User>> getAllUsers(int pageNum, int pageSize) {
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setPageNum(pageNum);
        searchCondition.setSize(pageSize);
        ApiResponse<List<User>> apiResponse = userRep.getPageBySearchCondition(searchCondition);
        return apiResponse;
    }
}
