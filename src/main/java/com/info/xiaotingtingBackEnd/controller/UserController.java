package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.User;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.repository.UserRep;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.UserService;
import com.info.xiaotingtingBackEnd.socket.SenderEventHandler;
import com.info.xiaotingtingBackEnd.util.DataCheckUtil;
import com.info.xiaotingtingBackEnd.util.EntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
    UserService userService;

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
    public ApiResponse<User> addUser(@RequestBody User user) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        if (userService.findByAccount(user.getAccount()) != null) {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("账户已存在");
        } else {
            user.setNickname(user.getAccount());
            userService.save(user);
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("注册成功");
            apiResponse.setData(user);
        }
        return apiResponse;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ApiResponse login(@RequestBody Map<String, Object> params) {
        String account = (String) params.get("account");
        String password = (String) params.get("password");
        ApiResponse<User> apiResponse = new ApiResponse<User>();
        System.out.println("login : "+ account + ":" + password);
        User user = userService.findByAccountAndPassword(account, password);
        if (user != null) {
            System.out.println(user + " " + account + ":" + password);
            //若有其他设备登陆则踢下线
            handler.offline(user.getUserId(), user.getToken());

            user.setToken(EntityUtil.getIdByTimeStampAndRandom());
            user = userService.save(user);
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
    public ApiResponse rememberMe(@RequestBody Map<String, String> params) {
        String userId = params.get("userId");
        String token = params.get("token");
        ApiResponse<User> apiResponse = new ApiResponse<User>();
        System.out.println(userId + ":" + token);
        User user = userService.findOne(userId);
        if (user != null && user.getToken().equals(token)) {
            System.out.println(user + " " + userId + ":" + token);
            user.setToken(EntityUtil.getIdByTimeStampAndRandom());
            user = userService.save(user);
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("登陆成功");
            apiResponse.setData(user);
        } else {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("账号在其他设备上被登录,请重新登录");
            apiResponse.setData(new User());
        }
        return apiResponse;
    }

    @RequestMapping(value = "getAllUsers", method = RequestMethod.POST)
    public ApiResponse<List<User>> getAllUsers(@RequestBody Map<String, Integer> params) {
        int pageNum = params.get("pageNum");
        int pageSize = params.get("pageSize");
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setPageNum(pageNum);
        searchCondition.setSize(pageSize);
        ApiResponse<List<User>> apiResponse = userService.getPageBySearchCondition(searchCondition);
        return apiResponse;
    }

    @RequestMapping(value = "getMyInformation", method = RequestMethod.POST)
    public ApiResponse<User> getMyInformation(@RequestBody Map<String, String> params) {
        String userId = params.get("userId");
        String token = params.get("token");
        ApiResponse<User> apiResponse = new ApiResponse<>();
        User result = userService.findOne(userId);
        if (result.getToken().equals(token)) {
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("获取个人信息成功");
            apiResponse.setData(result);
        } else {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("token验证失败");
        }
        return apiResponse;
    }

    @RequestMapping(value = "updateMyInformation", method = RequestMethod.POST)
    public ApiResponse<User> updateMyInformation(@RequestBody User user) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        User result = userService.findOne(user.getUserId());
        if (!DataCheckUtil.isEmpty(user.getAvatar())) {
            result.setAvatar(user.getAvatar());
        } else if (!DataCheckUtil.isEmpty(user.getNickname())) {
            result.setNickname(user.getNickname());
        } else if (!DataCheckUtil.isEmpty(user.getTelephone())) {
            result.setTelephone(user.getTelephone());
        } else if (!DataCheckUtil.isEmpty(user.getSex())) {
            result.setSex(user.getSex());
        } else if (!DataCheckUtil.isEmpty(user.getBirthday())) {
            result.setBirthday(user.getBirthday());
        } else if (!DataCheckUtil.isEmpty(user.getRegion())) {
            result.setRegion(user.getRegion());
        }
        result = userService.save(result);
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("修改成功");
        apiResponse.setData(result);
        return apiResponse;
    }

    @RequestMapping(value = "getUserInfo", method = RequestMethod.POST)
    public ApiResponse<User> getUserInfo(@RequestBody Map<String, String> params) {
        String userId = params.get("userId");
        User result = userService.findOne(userId);
        User info = new User();
        info.setUserId(result.getUserId());
        info.setNickname(result.getNickname());
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("获取成功");
        apiResponse.setData(info);
        return apiResponse;
    }

}
