package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.Attendance;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.service.AttendanceService;
import com.info.xiaotingtingBackEnd.util.FormatDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("attendance")
public class AttendanceController {

    @Autowired
    AttendanceService attendanceService;

    @RequestMapping(value = "getAttendance", method = RequestMethod.POST)
    public ApiResponse<Attendance> getAttendance(@RequestBody Map<String, String> params) {
        return attendanceService.getAttendance(params.get("userId"));
    }

    @RequestMapping(value = "punchIn", method = RequestMethod.POST)
    public ApiResponse<Attendance> punchIn(@RequestBody Attendance attendance) {
        return attendanceService.punchIn(attendance);
    }

    @RequestMapping(value = "punchOut", method = RequestMethod.POST)
    public ApiResponse<Attendance> punchOut(@RequestBody Attendance attendance) {
        return attendanceService.punchOut(attendance);
    }
}
