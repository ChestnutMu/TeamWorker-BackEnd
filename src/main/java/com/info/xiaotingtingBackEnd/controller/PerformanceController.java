package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.DayReport;
import com.info.xiaotingtingBackEnd.model.MonthReport;
import com.info.xiaotingtingBackEnd.model.Performance;
import com.info.xiaotingtingBackEnd.model.WeekReport;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.base.SearchBean;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.*;
import com.info.xiaotingtingBackEnd.util.DataCheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/2 17:47:27
 * Description：绩效
 * Email: xiaoting233zhang@126.com
 */
@RestController
@RequestMapping("performance")
public class PerformanceController {

    @Autowired
    PerformanceService performanceService;

    @Autowired
    TeamService teamService;

    /**
     * 提交绩效
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "submitPerformance", method = RequestMethod.POST)
    public ApiResponse<Object> submitPerformance(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        /*团队id*/
        String teamId = params.get("teamId");
        /*用户昵称*/
        String userNickname = params.get("userNickname");
        /*用户头像*/
        String userAvatar = params.get("userAvatar");
        /*上月工作任务*/
        String lastWorkTask = params.get("lastWorkTask");
        /*上月完成工作*/
        String finishedWork = params.get("finishedWork");
        /*任务达成率*/
        String reachRate = params.get("reachRate");
        /*上月工作自评*/
        String selfEvaluation = params.get("selfEvaluation");
        /*本月工作任务*/
        String thisWorkTask = params.get("thisWorkTask");
        /*本月工作计划*/
        String workPlan = params.get("workPlan");
        /*图片*/
        String photo = params.get("photo");
        performanceService.submitPerformance(userId, userNickname, userAvatar, teamId, lastWorkTask, finishedWork, reachRate, selfEvaluation, thisWorkTask, workPlan, photo);
        ApiResponse response = new ApiResponse<>(HttpResponseCodes.SUCCESS, "绩效自评提交成功");
        return response;
    }

    /**
     * 获取个人绩效自评列表
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "getPerformances", method = RequestMethod.POST)
    public ApiResponse<List<Performance>> getPerformances(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String teamId = params.get("teamId");
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSearchCondition(params);
        searchCondition.addSearchBean("userId", userId, SearchBean.OPERATOR_EQ);
        searchCondition.addSearchBean("teamId", teamId, SearchBean.OPERATOR_EQ);
        searchCondition.addSortBean("commitTime", "asc", SearchBean.OPERATOR_SORT);
        ApiResponse<List<Performance>> response = performanceService.getPageBySearchCondition(searchCondition);
        return response;
    }

    /**
     * 获取团队绩效自评列表
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "getPerformancesForTeam", method = RequestMethod.POST)
    public ApiResponse<List<Performance>> getPerformancesForTeam(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String teamId = params.get("teamId");
        String teamUserId = params.get("teamUserId");
        if (!userId.equals(teamUserId))
            teamService.checkPermission(teamId, userId, teamUserId);
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSearchCondition(params);
        searchCondition.addSearchBean("teamId", teamId, SearchBean.OPERATOR_EQ);
        if (!DataCheckUtil.isEmpty(teamUserId))
            searchCondition.addSearchBean("userId", teamUserId, SearchBean.OPERATOR_EQ);
        searchCondition.addSortBean("commitTime", "asc", SearchBean.OPERATOR_SORT);
        ApiResponse<List<Performance>> response = performanceService.getPageBySearchCondition(searchCondition);
        return response;
    }

}
