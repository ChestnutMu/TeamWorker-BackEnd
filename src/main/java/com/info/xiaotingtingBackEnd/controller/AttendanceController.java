package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.Attendance;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.repository.AttendanceRep;
import com.info.xiaotingtingBackEnd.util.FormatDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/1/3 13:48:57
 * Description：AttendanceController
 * Email: xiaoting233zhang@126.com
 */
@RestController
@RequestMapping("Attendance")
public class AttendanceController {
    @Autowired
    AttendanceRep attendanceRep;

    @RequestMapping(value = "getAttendance", method = RequestMethod.POST)
    public ApiResponse<Attendance> getAttendance(@RequestBody Map<String, String> params) {
        String userId = params.get("userId");
        ApiResponse<Attendance> apiResponse = new ApiResponse<>();
        Date today = new Date();
        Attendance lastAttendance = attendanceRep.findTopByUserIdOrderByPunchInTimeDesc(userId);
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        if (lastAttendance != null && lastAttendance.getPunchInTime() != null && FormatDateUtil.isSameDay(today, lastAttendance.getPunchInTime())) {
            apiResponse.setData(lastAttendance);
            apiResponse.setMessage("获取今天的考勤记录成功");
        } else {
            apiResponse.setMessage("今天无考勤记录");
        }
        return apiResponse;
    }

    @RequestMapping(value = "punchIn", method = RequestMethod.POST)
    public ApiResponse<Attendance> punchIn(@RequestBody Attendance attendance) {
        ApiResponse<Attendance> apiResponse = new ApiResponse<>();
        Date today = new Date();
        Attendance lastAttendance = attendanceRep.findTopByUserIdOrderByPunchInTimeDesc(attendance.getUserId());
        if (lastAttendance != null && lastAttendance.getPunchOutTime() != null && FormatDateUtil.isSameDay(today, lastAttendance.getPunchOutTime())) {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("今天已打过下班卡");
        } else {
            attendance.setPunchInTime(new Date());
            attendanceRep.save(attendance);
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("上班打卡成功");
            apiResponse.setData(attendance);
        }

        return apiResponse;
    }

    @RequestMapping(value = "punchOut", method = RequestMethod.POST)
    public ApiResponse<Attendance> punchOut(@RequestBody Attendance attendance) {
        ApiResponse<Attendance> apiResponse = new ApiResponse<>();
        Date today = new Date();
        Attendance lastAttendance = attendanceRep.findTopByUserIdOrderByPunchInTimeDesc(attendance.getUserId());
        if (lastAttendance != null && lastAttendance.getPunchInTime() != null && FormatDateUtil.isSameDay(today, lastAttendance.getPunchInTime())) {
            lastAttendance.setAltitude(attendance.getAltitude());
            lastAttendance.setLatitude(attendance.getLatitude());
            lastAttendance.setDetailAddress(attendance.getDetailAddress());
            lastAttendance.setPunchOutTime(new Date());

            lastAttendance = attendanceRep.save(lastAttendance);
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
            apiResponse.setMessage("下班打卡成功");
            apiResponse.setData(lastAttendance);
        } else {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("今天还未打上班卡");
        }

        return apiResponse;
    }
}
