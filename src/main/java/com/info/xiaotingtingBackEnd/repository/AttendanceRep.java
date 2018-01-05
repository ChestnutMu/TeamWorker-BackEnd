package com.info.xiaotingtingBackEnd.repository;

import com.info.xiaotingtingBackEnd.model.Attendance;
import com.info.xiaotingtingBackEnd.repository.base.BaseRepository;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/1/3 13:49:56
 * Description：
 * Email: xiaoting233zhang@126.com
 */
public interface AttendanceRep extends BaseRepository<Attendance, String> {

    Attendance findTopByUserId(String userId);
}
