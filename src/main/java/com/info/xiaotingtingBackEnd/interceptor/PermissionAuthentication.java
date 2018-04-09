package com.info.xiaotingtingBackEnd.interceptor;

import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.service.PermissionService;
import com.info.xiaotingtingBackEnd.service.UserPermissionRelationService;
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
 * CreateTime：at 2018/4/8 13:14:36
 * Description：权限验证
 * Email: xiaoting233zhang@126.com
 */

public class PermissionAuthentication implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Autowired
    UserPermissionRelationService relationService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info("RequestURI " + httpServletRequest.getRequestURI());
        logger.info("method " + httpServletRequest.getRemoteAddr());
        String userId = httpServletRequest.getHeader("uid");
        String uri = httpServletRequest.getRequestURI();
        if (relationService.getUserPermissionRelation(userId, uri) != null) {
            logger.info("查询到该用户对该Uri的请求权限");
            return true;
        } else {
            logger.info("无查询到该用户对该Uri的请求权限");
            throw new PlatformException(-1, "无查询到该用户对该Uri的请求权限");
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
