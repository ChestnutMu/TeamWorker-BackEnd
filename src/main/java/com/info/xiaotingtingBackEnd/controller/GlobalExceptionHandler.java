package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by king on 16:49 2018/4/9
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
