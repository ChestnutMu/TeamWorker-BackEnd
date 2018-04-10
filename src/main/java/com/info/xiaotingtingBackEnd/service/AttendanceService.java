package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.Attendance;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.AttendanceRep;
import com.info.xiaotingtingBackEnd.repository.base.SearchBean;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.base.BaseService;
import com.info.xiaotingtingBackEnd.util.DataCheckUtil;
import com.info.xiaotingtingBackEnd.util.FormatDateUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/3 16:27:29
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Service
public class AttendanceService extends BaseService<Attendance, String, AttendanceRep> {

    @Override
    public AttendanceRep getRepo() {
        return attendanceRep;
    }

//    public ApiResponse<Attendance> getAttendance(String userId) {
//        ApiResponse<Attendance> apiResponse = new ApiResponse<>();
//        Date today = new Date();
//        Attendance lastAttendance = attendanceRep.findTopByUserIdOrderByPunchInTimeDesc(userId);
//        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
//        if (lastAttendance != null && lastAttendance.getPunchInTime() != null && FormatDateUtil.isSameDay(today, lastAttendance.getPunchInTime())) {
//            apiResponse.setData(lastAttendance);
//            apiResponse.setMessage("获取今天的考勤记录成功");
//        } else {
//            apiResponse.setMessage("今天无考勤记录");
//        }
//        return apiResponse;
//    }
//
//    public ApiResponse<Attendance> punchIn(Attendance attendance) {
//        ApiResponse<Attendance> apiResponse = new ApiResponse<>();
//        Date today = new Date();
//
//        Attendance lastAttendance = attendanceRep.findTopByUserIdOrderByPunchInTimeDesc(attendance.getUserId());
//        if (lastAttendance != null && lastAttendance.getPunchOutTime() != null && FormatDateUtil.isSameDay(today, lastAttendance.getPunchOutTime())) {
//            apiResponse.setStatus(HttpResponseCodes.FAILED);
//            apiResponse.setMessage("今天已打过下班卡");
//        } else {
//            attendance.setPunchInTime(new Date());
//            attendanceRep.save(attendance);
//            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
//            apiResponse.setMessage("上班打卡成功");
//            apiResponse.setData(attendance);
//        }
//        return apiResponse;
//    }
//
//    public ApiResponse<Attendance> punchOut(Attendance attendance) {
//        ApiResponse<Attendance> apiResponse = new ApiResponse<>();
//        Date today = new Date();
//        Attendance lastAttendance = attendanceRep.findTopByUserIdOrderByPunchInTimeDesc(attendance.getUserId());
//        if (lastAttendance != null && lastAttendance.getPunchInTime() != null && FormatDateUtil.isSameDay(today, lastAttendance.getPunchInTime())) {
//            lastAttendance.setAltitude(attendance.getAltitude());
//            lastAttendance.setLatitude(attendance.getLatitude());
//            lastAttendance.setPunchOutAddress(attendance.getPunchOutAddress());
//            lastAttendance.setPunchOutTime(new Date());
//
//            lastAttendance = attendanceRep.save(lastAttendance);
//            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
//            apiResponse.setMessage("下班打卡成功");
//            apiResponse.setData(lastAttendance);
//        } else {
//            apiResponse.setStatus(HttpResponseCodes.FAILED);
//            apiResponse.setMessage("今天还未打上班卡");
//        }
//
//        return apiResponse;
//    }

    public Attendance punchClock(String userId, String teamId, String punchClockAddress, String picture) throws PlatformException {
        if (DataCheckUtil.isEmpty(teamId))
            throw new PlatformException(-1, "必须在一个团队里面打卡");
        if (DataCheckUtil.isEmpty(punchClockAddress))
            throw new PlatformException(-1, "必须要上传目前所在地址");
        Long count = teamRelationRep.countAllByTeamIdAndUserId(teamId, userId);
        if (count == null || count <= 0)
            throw new PlatformException(-1, "你不属于该团队");
        Attendance attendance = attendanceRep.findTopByUserIdAndTeamIdAndPunchOutTimeOrderByPunchInTimeDesc(userId, teamId, null);
        if (attendance == null) {
            logger.info("之前没有在该团队打过卡（上班），现在默认是打上班卡");
            attendance = new Attendance();
            attendance.setUserId(userId);
            attendance.setTeamId(teamId);
            attendance.setPunchInAddress(punchClockAddress);
            attendance.setPunchInPicture(picture);
            attendance.setPunchInTime(new Date());
            attendanceRep.save(attendance);
            return attendance;
        } else {
            logger.info("之前有在该团队打过卡（上班），现在默认是打下班卡");
            attendance.setPunchOutAddress(punchClockAddress);
            attendance.setPunchOutPicture(picture);
            attendance.setPunchOutTime(new Date());
            attendanceRep.save(attendance);
            return attendance;
        }
    }

    /**
     * 自动把未下班打卡的记录给打卡下班
     */
    public void autoPunchClock() {
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.addSearchBean("punchOutTime", null, SearchBean.OPERATOR_IS_NULL);
        List<Attendance> attendanceList = attendanceRep.getListBySearchCondition(searchCondition);
        logger.info("autoPunchClock 自动打卡（补下班打卡） 处理记录数为" + attendanceList.size());
        Date now = new Date();
        for (Attendance attendance : attendanceList) {
            attendance.setPunchOutTime(now);
        }
        attendanceRep.save(attendanceList);
    }
}
