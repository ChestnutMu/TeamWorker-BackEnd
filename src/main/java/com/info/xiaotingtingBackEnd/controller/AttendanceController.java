package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.model.Attendance;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.base.SearchBean;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.AttendanceService;
import com.info.xiaotingtingBackEnd.service.TeamService;
import com.info.xiaotingtingBackEnd.util.DataCheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/1/3 13:48:57
 * Description：AttendanceController
 * Email: xiaoting233zhang@126.com
 */
@RestController
@RequestMapping("attendance")
public class AttendanceController {

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    TeamService teamService;

//    @RequestMapping(value = "getAttendance", method = RequestMethod.POST)
//    public ApiResponse<Attendance> getAttendance(@RequestBody Map<String, String> params) {
//        return attendanceService.getAttendance(params.get("userId"));
//    }
//
//    @RequestMapping(value = "punchIn", method = RequestMethod.POST)
//    public ApiResponse<Attendance> punchIn(@RequestBody Attendance attendance) {
//        return attendanceService.punchIn(attendance);
//    }
//
//    @RequestMapping(value = "punchOut", method = RequestMethod.POST)
//    public ApiResponse<Attendance> punchOut(@RequestBody Attendance attendance) {
//        return attendanceService.punchOut(attendance);
//    }

    /**
     * 打卡
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "punchClock", method = RequestMethod.POST)
    public ApiResponse<Attendance> punchClock(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        ApiResponse<Attendance> response = new ApiResponse<>(0, "打卡成功");
        String teamId = params.get("teamId");
        String picture = params.get("picture");
        String punchClockAddress = params.get("punchClockAddress");
        Attendance attendance = attendanceService.punchClock(userId, teamId, punchClockAddress, picture);
        response.setData(attendance);
        return response;
    }

    /**
     * 获取个人打卡记录
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "getPunchClockRecords", method = RequestMethod.POST)
    public ApiResponse<List<Attendance>> getPunchClockRecords(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String teamId = params.get("teamId");
        long startTime = Long.parseLong(params.get("startTime"));
        long endTime = Long.parseLong(params.get("endTime"));
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSearchCondition(params);
        searchCondition.addSearchBean("userId", userId, SearchBean.OPERATOR_EQ);
        if (!DataCheckUtil.isEmpty(teamId))
            searchCondition.addSearchBean("teamId", teamId, SearchBean.OPERATOR_EQ);
        searchCondition.addSearchBean("punchInTime", new Date(startTime), SearchBean.OPERATOR_GE);
        searchCondition.addSearchBean("punchInTime", new Date(endTime), SearchBean.OPERATOR_LE);
        searchCondition.addSortBean("punchInTime", "desc", SearchBean.OPERATOR_SORT);
        ApiResponse<List<Attendance>> response = attendanceService.getPageBySearchCondition(searchCondition);
        return response;
    }

    /**
     * 获取团队人员打卡记录
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "getPunchClockRecordsByTeam", method = RequestMethod.POST)
    public ApiResponse<List<Attendance>> getPunchClockRecordsByTeam(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String teamId = params.get("teamId");
        String teamUserId = params.get("teamUserId");
        if (!userId.equals(teamUserId))//如果是查看自己账户记录则不限制
            teamService.checkTeamAuthForPunchClockRecords(teamId, userId, teamUserId);
        long startTime = Long.parseLong(params.get("startTime"));
        long endTime = Long.parseLong(params.get("endTime"));
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSearchCondition(params);
        if (!DataCheckUtil.isEmpty(teamUserId))
            searchCondition.addSearchBean("userId", teamUserId, SearchBean.OPERATOR_EQ);
        if (!DataCheckUtil.isEmpty(teamId))
            searchCondition.addSearchBean("teamId", teamId, SearchBean.OPERATOR_EQ);
        searchCondition.addSearchBean("punchInTime", new Date(startTime), SearchBean.OPERATOR_GE);
        searchCondition.addSearchBean("punchInTime", new Date(endTime), SearchBean.OPERATOR_LE);
        searchCondition.addSortBean("punchInTime", "desc", SearchBean.OPERATOR_SORT);
        ApiResponse<List<Attendance>> response = attendanceService.getPageBySearchCondition(searchCondition);
        return response;
    }
}
