package com.info.xiaotingtingBackEnd.repository;

import com.info.xiaotingtingBackEnd.model.DayReport;
import com.info.xiaotingtingBackEnd.model.WeekReport;
import com.info.xiaotingtingBackEnd.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/3 09:28:29
 * Description：周报Repository
 * Email: xiaoting233zhang@126.com
 */
@Repository
public interface WeekReportRep extends BaseRepository<WeekReport, String> {

}
