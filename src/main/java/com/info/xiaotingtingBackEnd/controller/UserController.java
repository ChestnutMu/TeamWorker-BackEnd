package com.info.xiaotingtingBackEnd.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.info.xiaotingtingBackEnd.constans.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.User;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.repository.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserRep userRep;


    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String test() {

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
    public ApiResponse login(String account,String password) {
        ApiResponse<User> apiResponse = new ApiResponse<User>();
        System.out.println(account+":"+password);
        User user = userRep.findByAccountAndPassword(account, password);
        if (user != null) {
            System.out.println(user+" "+account+":"+password);
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("登陆成功");
            apiResponse.setData(user);
        } else {
            System.out.println(user+" "+account+":"+password);
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("用户名或密码错误");
            apiResponse.setData(new User());
        }
        return apiResponse;
    }
}
