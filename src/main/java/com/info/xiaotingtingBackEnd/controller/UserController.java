package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.Message;
import com.info.xiaotingtingBackEnd.model.NewFriendRequest;
import com.info.xiaotingtingBackEnd.model.User;
import com.info.xiaotingtingBackEnd.model.UserRelation;
import com.info.xiaotingtingBackEnd.model.vo.NewFriendRequestVo;
import com.info.xiaotingtingBackEnd.model.vo.UserVo;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.UserRep;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.NewFriendRequestService;
import com.info.xiaotingtingBackEnd.service.UserService;
import com.info.xiaotingtingBackEnd.socket.SenderEventHandler;
import com.info.xiaotingtingBackEnd.util.DataCheckUtil;
import com.info.xiaotingtingBackEnd.util.EntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    NewFriendRequestService friendRequestService;

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

    /**
     * 注册
     *
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "unauth/register", method = RequestMethod.POST)
    public ApiResponse<User> register(@RequestBody Map<String, String> params) throws Exception {
        String telephone = params.get("telephone");
        String verificationCode = params.get("verificationCode");
        String password = params.get("password");
        User user = userService.register(telephone, verificationCode, password);
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setData(user);
        return apiResponse;
    }

    /**
     * 登录
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "unauth/login", method = RequestMethod.POST)
    public ApiResponse<User> login(@RequestBody Map<String, Object> params) throws Exception {
        String telephone = (String) params.get("telephone");
        String password = (String) params.get("password");
        ApiResponse<User> apiResponse = new ApiResponse<>();
        User user = userService.login(telephone, password);
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("登陆成功");
        apiResponse.setData(user);
        return apiResponse;
    }

    @RequestMapping(value = "rememberMe", method = RequestMethod.POST)
    public ApiResponse<User> rememberMe(@RequestHeader("uid") String userId) throws PlatformException {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        User user = userService.rememberMe(userId);
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("登陆成功");
        apiResponse.setData(user);
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
        result.setPassword(null);
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
        User result = userService.findOne(params.get("userId"));
        ApiResponse<User> apiResponse = new ApiResponse<>();
        if (result != null) {
            result.setPassword(null);
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("获取成功");
            apiResponse.setData(result);
        } else {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("获取用户信息失败");
        }
        return apiResponse;
    }

    @RequestMapping(value = "searchUser", method = RequestMethod.POST)
    public ApiResponse<List<User>> searchUser(@RequestBody Map<String, String> params) {
        String keyword = params.get("keyword");
        List<User> result = userService.searchUser(keyword);
        ApiResponse<List<User>> apiResponse = new ApiResponse<>();
        if (result != null && !result.isEmpty()) {
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("搜索成功");
            apiResponse.setData(result);
        } else {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("无搜索到该账号");
        }
        return apiResponse;
    }


    @RequestMapping(value = "addUserRelation", method = RequestMethod.POST)
    public ApiResponse<User> addUserRelation(@RequestHeader("uid") String uid, @RequestBody Map<String, String> params) {
        String userId = params.get("userId");
        String requestId = params.get("requestId");
        ApiResponse<User> apiResponse = new ApiResponse<>();
        User user = userService.findOne(userId);
        NewFriendRequest newFriendRequest = friendRequestService.findOne(requestId);
        if (user == null) {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("用户不存在");
        } else if (newFriendRequest == null) {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("好友请求不存在");
        } else {
            if (userService.addUserRelation(uid, userId)) {
                newFriendRequest.setAccepted(true);
                if (friendRequestService.save(newFriendRequest) != null) {
                    apiResponse.setStatus(HttpResponseCodes.SUCCESS);
                    apiResponse.setMessage("添加好友成功");
                    user.setPassword(null);
                    apiResponse.setData(user);
                } else {
                    apiResponse.setStatus(HttpResponseCodes.FAILED);
                    apiResponse.setMessage("添加好友失败");
                }
            } else {
                apiResponse.setStatus(HttpResponseCodes.FAILED);
                apiResponse.setMessage("添加好友失败");
            }
        }
        return apiResponse;
    }

    @RequestMapping(value = "sendFriendRequest", method = RequestMethod.POST)
    public ApiResponse<User> sendFriendRequest(@RequestHeader("uid") String requesterId, @RequestBody Map<String, String> params) {
        String recipientId = params.get("userId");
        String authenticationMessage = params.get("authenticationMessage");
        ApiResponse<User> apiResponse = new ApiResponse<>();
        User user = userService.findOne(recipientId);
        if (user == null) {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("用户不存在");
        } else {
            NewFriendRequest newFriendRequest = new NewFriendRequest();
            newFriendRequest.setRequesterId(requesterId);
            newFriendRequest.setRecipientId(recipientId);
            newFriendRequest.setTime(new Date());
            newFriendRequest.setSend(false);
            newFriendRequest.setAccepted(false);
            newFriendRequest.setAuthenticationMessage(authenticationMessage);

            friendRequestService.sendNewFriendRequest(newFriendRequest);
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("已发送好友请求");
            apiResponse.setData(user);
        }
        return apiResponse;
    }

    @RequestMapping(value = "isMyFriend", method = RequestMethod.POST)
    public ApiResponse<Boolean> isMyFriend(@RequestHeader("uid") String uid, @RequestBody Map<String, String> params) {
        String userId = params.get("userId");
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        if (userService.isMyFriend(userId, uid) || userService.isMyFriend(uid, userId)) {
            apiResponse.setData(true);
            apiResponse.setMessage("是我的好友");
        } else {
            apiResponse.setData(false);
            apiResponse.setMessage("不是我的好友");
        }
        return apiResponse;
    }

    /**
     * 根据userId其接收到的好友请求消息
     *
     * @return
     */
    @RequestMapping(value = "getMyFriends", method = RequestMethod.POST)
    public ApiResponse<List<UserVo>> getMyFriends(@RequestHeader String uid) {
        ApiResponse<List<UserVo>> apiResponse = new ApiResponse<>();
        List<UserVo> requestList = userService.getMyFriends(uid);
        apiResponse.setMaxCount(requestList.size());
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("获取我的好友列表成功");
        apiResponse.setData(requestList);
        return apiResponse;
    }

    @RequestMapping(value = "deleteUserRelation", method = RequestMethod.POST)
    public ApiResponse<Object> deleteUserRelation(@RequestHeader("uid") String uid, @RequestBody Map<String, String> params) {
        String userId = params.get("userId");
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        User user = userService.findOne(userId);
        if (user == null) {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("用户不存在");
        } else {
            userService.deleteUserRelation(uid, userId);
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("删除好友成功");
        }
        return apiResponse;
    }

}
