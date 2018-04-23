package com.info.xiaotingtingBackEnd.interceptor;

import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/1 14:19:14
 * Description：uid和token验证
 * Email: xiaoting233zhang@126.com
 */
public class IdAuthentication implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info("RequestURI " + httpServletRequest.getRequestURI());
        logger.info("method " + httpServletRequest.getRemoteAddr());
        String uid = httpServletRequest.getHeader("uid");
        String token = httpServletRequest.getHeader("token");
        logger.info("uid " + uid);
        logger.info("token " + token);
        if (userService.idAuth(uid, token)) {
            logger.info("身份认证成功");
            return true;
        } else {
            logger.info("身份认证失败");
            throw new PlatformException(-1, "身份认证失败");
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
