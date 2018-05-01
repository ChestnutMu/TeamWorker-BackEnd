package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.constants.WorkOffConstants;
import com.info.xiaotingtingBackEnd.model.DayReport;
import com.info.xiaotingtingBackEnd.model.MonthReport;
import com.info.xiaotingtingBackEnd.model.WeekReport;
import com.info.xiaotingtingBackEnd.model.WorkOff;
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
 * Description：工作汇报
 * Email: xiaoting233zhang@126.com
 */
@RestController
@RequestMapping("report")
public class ReportController {

    @Autowired
    DayReportService dayReportService;

    @Autowired
    WeekReportService weekReportService;

    @Autowired
    MonthReportService monthReportService;

    @Autowired
    TeamService teamService;

    /**
     * 提交日报
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "submitDayReport", method = RequestMethod.POST)
    public ApiResponse<Object> submitDayReport(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        /*团队id*/
        String teamId = params.get("teamId");
        /*用户昵称*/
        String userNickname = params.get("userNickname");
        /*用户头像*/
        String userAvatar = params.get("userAvatar");
        /*已完成工作*/
        String finishedWork = params.get("finishedWork");
        /*未完成工作*/
        String unfinishedWork = params.get("unfinishedWork");
        /*需协调工作*/
        String needCoordinatedWork = params.get("needCoordinatedWork");
        /*备注*/
        String remarks = params.get("remarks");
        /*图片*/
        String photo = params.get("photo");
        dayReportService.submitDayReport(userId, userNickname, userAvatar, teamId, finishedWork, unfinishedWork, needCoordinatedWork, remarks, photo);
        ApiResponse response = new ApiResponse<>(HttpResponseCodes.SUCCESS, "日报提交成功");
        return response;
    }

    /**
     * 提交周报
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "submitWeekReport", method = RequestMethod.POST)
    public ApiResponse<Object> submitWeekReport(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        /*团队id*/
        String teamId = params.get("teamId");
        /*用户昵称*/
        String userNickname = params.get("userNickname");
        /*用户头像*/
        String userAvatar = params.get("userAvatar");
        /*本周完成工作*/
        String finishedWork = params.get("finishedWork");
        /*本周工作总结*/
        String workSummary = params.get("workSummary");
        /*下周工作计划*/
        String workPlan = params.get("workPlan");
        /*需协调工作*/
        String needCoordinatedWork = params.get("needCoordinatedWork");
        /*备注*/
        String remarks = params.get("remarks");
        /*图片*/
        String photo = params.get("photo");
        weekReportService.submitWeekReport(userId, userNickname, userAvatar, teamId, finishedWork, workPlan, workSummary, needCoordinatedWork, remarks, photo);
        ApiResponse response = new ApiResponse<>(HttpResponseCodes.SUCCESS, "周报提交成功");
        return response;
    }

    /**
     * 提交月报
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "submitMonthReport", method = RequestMethod.POST)
    public ApiResponse<Object> submitMonthReport(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        /*团队id*/
        String teamId = params.get("teamId");
        /*用户昵称*/
        String userNickname = params.get("userNickname");
        /*用户头像*/
        String userAvatar = params.get("userAvatar");
        /*本月工作内容*/
        String workContent = params.get("workContent");
        /*本月完成工作*/
        String finishedWork = params.get("finishedWork");
        /*下月工作计划*/
        String workPlan = params.get("workPlan");
        /*下月工作计划*/
        String workSummary = params.get("workSummary");
        /*需协调工作*/
        String needCoordinatedWork = params.get("needCoordinatedWork");
        /*备注*/
        String remarks = params.get("remarks");
        /*图片*/
        String photo = params.get("photo");
        monthReportService.submitMonthReport(userId, userNickname, userAvatar, teamId, workContent, finishedWork, workPlan, workSummary, needCoordinatedWork, remarks, photo);
        ApiResponse response = new ApiResponse<>(HttpResponseCodes.SUCCESS, "月报提交成功");
        return response;
    }

    /**
     * 获取个人日报列表
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "getDayReports", method = RequestMethod.POST)
    public ApiResponse<List<DayReport>> getDayReports(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String teamId = params.get("teamId");
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSearchCondition(params);
        searchCondition.addSearchBean("userId", userId, SearchBean.OPERATOR_EQ);
        searchCondition.addSearchBean("teamId", teamId, SearchBean.OPERATOR_EQ);
        searchCondition.addSortBean("commitTime", "asc", SearchBean.OPERATOR_SORT);
        ApiResponse<List<DayReport>> response = dayReportService.getPageBySearchCondition(searchCondition);
        return response;
    }

    /**
     * 获取个人周报列表
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "getWeekReports", method = RequestMethod.POST)
    public ApiResponse<List<WeekReport>> getWeekReports(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String teamId = params.get("teamId");
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSearchCondition(params);
        searchCondition.addSearchBean("userId", userId, SearchBean.OPERATOR_EQ);
        searchCondition.addSearchBean("teamId", teamId, SearchBean.OPERATOR_EQ);
        searchCondition.addSortBean("commitTime", "asc", SearchBean.OPERATOR_SORT);
        ApiResponse<List<WeekReport>> response = weekReportService.getPageBySearchCondition(searchCondition);
        return response;
    }

    /**
     * 获取个人月报列表
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "getMonthReports", method = RequestMethod.POST)
    public ApiResponse<List<MonthReport>> getMonthReports(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String teamId = params.get("teamId");
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSearchCondition(params);
        searchCondition.addSearchBean("userId", userId, SearchBean.OPERATOR_EQ);
        searchCondition.addSearchBean("teamId", teamId, SearchBean.OPERATOR_EQ);
        searchCondition.addSortBean("commitTime", "asc", SearchBean.OPERATOR_SORT);
        ApiResponse<List<MonthReport>> response = monthReportService.getPageBySearchCondition(searchCondition);
        return response;
    }

    /**
     * 获取团队日报列表
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "getDayReportsForTeam", method = RequestMethod.POST)
    public ApiResponse<List<DayReport>> getDayReportsForTeam(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
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
        ApiResponse<List<DayReport>> response = dayReportService.getPageBySearchCondition(searchCondition);
        return response;
    }

    /**
     * 获取团队周报列表
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "getWeekReportsForTeam", method = RequestMethod.POST)
    public ApiResponse<List<WeekReport>> getWeekReportsForTeam(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
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
        ApiResponse<List<WeekReport>> response = weekReportService.getPageBySearchCondition(searchCondition);
        return response;
    }

    /**
     * 获取团队月报列表
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "getMonthReportsForTeam", method = RequestMethod.POST)
    public ApiResponse<List<MonthReport>> getMonthReportsForTeam(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
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
        ApiResponse<List<MonthReport>> response = monthReportService.getPageBySearchCondition(searchCondition);
        return response;
    }

}
