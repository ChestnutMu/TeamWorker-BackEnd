package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.model.MonthReport;
import com.info.xiaotingtingBackEnd.model.Performance;
import com.info.xiaotingtingBackEnd.model.TeamRelation;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.MonthReportRep;
import com.info.xiaotingtingBackEnd.repository.PerformanceRep;
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
public class PerformanceService extends BaseService<Performance, String, PerformanceRep> {
    @Override
    public PerformanceRep getRepo() {
        return performanceRep;
    }

    public void submitPerformance(String userId, String userNickname, String userAvatar, String teamId, String lastWorkTask, String finishedWork, String workPlan, String reachRate, String selfEvaluation, String thisWorkTask, String photo) throws PlatformException {
        if (DataCheckUtil.isEmpty(teamId))
            throw new PlatformException(-1, "必须选择团队");
        if (DataCheckUtil.isEmpty(lastWorkTask))
            throw new PlatformException(-1, "上月工作任务不能为空");
        if (DataCheckUtil.isEmpty(finishedWork))
            throw new PlatformException(-1, "实际完成任务不能为空");
        if (DataCheckUtil.isEmpty(reachRate)) {
            throw new PlatformException(-1, "任务达成率不能为空");
        }
        if (DataCheckUtil.isEmpty(selfEvaluation)) {
            throw new PlatformException(-1, "上月工作自评不能为空");
        }
        if (DataCheckUtil.isEmpty(thisWorkTask)) {
            throw new PlatformException(-1, "本月工作任务不能为空");
        }
        if (DataCheckUtil.isEmpty(workPlan)) {
            throw new PlatformException(-1, "本月工作计划不能为空");
        }
        TeamRelation teamRelation = teamRelationRep.findOne(new TeamRelation.TeamRelationId(teamId, userId));
        if (teamRelation == null)
            throw new PlatformException(-1, "你不属于该团队");
        Performance performance = new Performance();
        performance.setUserId(userId);
        performance.setUserNickname(userNickname);
        performance.setUserAvatar(userAvatar);
        performance.setTeamId(teamId);
        performance.setLastWorkTask(lastWorkTask);
        performance.setWorkPlan(workPlan);
        performance.setReachRate(reachRate);
        performance.setSelfEvaluation(selfEvaluation);
        performance.setPhoto(photo);
        performance.setThisWorkTask(thisWorkTask);
        performance.setFinishedWork(finishedWork);
        performance.setCommitTime(new Date());
        performanceRep.save(performance);
    }
}
