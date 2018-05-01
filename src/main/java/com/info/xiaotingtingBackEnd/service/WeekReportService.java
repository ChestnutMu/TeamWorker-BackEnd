package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.model.DayReport;
import com.info.xiaotingtingBackEnd.model.TeamRelation;
import com.info.xiaotingtingBackEnd.model.WeekReport;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.DayReportRep;
import com.info.xiaotingtingBackEnd.repository.WeekReportRep;
import com.info.xiaotingtingBackEnd.service.base.BaseService;
import com.info.xiaotingtingBackEnd.util.DataCheckUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/5 21:09:53
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Service
public class WeekReportService extends BaseService<WeekReport, String, WeekReportRep> {
    @Override
    public WeekReportRep getRepo() {
        return weekReportRep;
    }

    public void submitWeekReport(String userId, String userNickname, String userAvatar, String teamId, String finishedWork, String workPlan, String workSummary, String needCoordinatedWork, String remarks, String photo) throws PlatformException {
        if (DataCheckUtil.isEmpty(teamId))
            throw new PlatformException(-1, "必须选择团队");
        if (DataCheckUtil.isEmpty(finishedWork))
            throw new PlatformException(-1, "本周完成工作不能为空");
        if (DataCheckUtil.isEmpty(workSummary))
            throw new PlatformException(-1, "下周工作总结不能为空");
        if (DataCheckUtil.isEmpty(workPlan))
            throw new PlatformException(-1, "下周工作计划不能为空");
        TeamRelation teamRelation = teamRelationRep.findOne(new TeamRelation.TeamRelationId(teamId, userId));
        if (teamRelation == null)
            throw new PlatformException(-1, "你不属于该团队");
        WeekReport weekReport = new WeekReport();
        weekReport.setUserId(userId);
        weekReport.setUserNickname(userNickname);
        weekReport.setUserAvatar(userAvatar);
        weekReport.setTeamId(teamId);
        weekReport.setFinishedWork(finishedWork);
        weekReport.setWorkPlan(workPlan);
        weekReport.setWorkSummary(workSummary);
        weekReport.setNeedCoordinatedWork(needCoordinatedWork);
        weekReport.setPhoto(photo);
        weekReport.setRemarks(remarks);
        weekReport.setCommitTime(new Date());
        weekReportRep.save(weekReport);
    }
}
