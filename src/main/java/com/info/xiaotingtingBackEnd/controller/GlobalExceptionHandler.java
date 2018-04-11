package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/10 15:23:00
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = PlatformException.class)
    @ResponseBody
    public ApiResponse<String> jsonErrorHandler(HttpServletRequest req, PlatformException e) throws Exception {
        ApiResponse<String> r = new ApiResponse<String>(e.getEvent(), e.getMessage());
        e.printStackTrace();
        return r;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResponse<String> jsonErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ApiResponse<String> r = new ApiResponse<String>(-1, e.toString());
        e.printStackTrace();
        return r;
    }
}
