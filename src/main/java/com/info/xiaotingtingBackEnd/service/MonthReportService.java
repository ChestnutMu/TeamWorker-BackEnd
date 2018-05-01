package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.model.MonthReport;
import com.info.xiaotingtingBackEnd.model.TeamRelation;
import com.info.xiaotingtingBackEnd.model.WeekReport;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.MonthReportRep;
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
public class MonthReportService extends BaseService<MonthReport, String, MonthReportRep> {
    @Override
    public MonthReportRep getRepo() {
        return monthReportRep;
    }

    public void submitMonthReport(String userId, String userNickname, String userAvatar, String teamId, String workContent, String finishedWork, String workPlan, String workSummary, String needCoordinatedWork, String remarks, String photo) throws PlatformException {
        if (DataCheckUtil.isEmpty(teamId))
            throw new PlatformException(-1, "必须选择团队");
        if (DataCheckUtil.isEmpty(workContent))
            throw new PlatformException(-1, "本月工作内容不能为空");
        if (DataCheckUtil.isEmpty(workSummary))
            throw new PlatformException(-1, "本月工作总结不能为空");
        if (DataCheckUtil.isEmpty(workPlan))
            throw new PlatformException(-1, "下月工作计划不能为空");
        TeamRelation teamRelation = teamRelationRep.findOne(new TeamRelation.TeamRelationId(teamId, userId));
        if (teamRelation == null)
            throw new PlatformException(-1, "你不属于该团队");
        MonthReport monthReport = new MonthReport();
        monthReport.setUserId(userId);
        monthReport.setUserNickname(userNickname);
        monthReport.setUserAvatar(userAvatar);
        monthReport.setTeamId(teamId);
        monthReport.setWorkContent(workContent);
        monthReport.setWorkPlan(workPlan);
        monthReport.setWorkSummary(workSummary);
        monthReport.setNeedCoordinatedWork(needCoordinatedWork);
        monthReport.setPhoto(photo);
        monthReport.setRemarks(remarks);
        monthReport.setCommitTime(new Date());
        monthReportRep.save(monthReport);
    }
}
