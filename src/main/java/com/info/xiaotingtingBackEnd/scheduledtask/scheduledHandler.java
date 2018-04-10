package com.info.xiaotingtingBackEnd.scheduledtask;

import com.info.xiaotingtingBackEnd.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/10 09:37:29
 * Description：定时器
 * Email: xiaoting233zhang@126.com
 */
@Component
public class scheduledHandler {
    @Autowired
    AttendanceService attendanceService;


    /**
     * 将过了凌晨的打卡记录给打卡下班
     */
    @Scheduled(cron = "0 0 0 * * ?")//每天凌晨0点执行一次
    public void autoPunchClock() {
        attendanceService.autoPunchClock();
    }
}
