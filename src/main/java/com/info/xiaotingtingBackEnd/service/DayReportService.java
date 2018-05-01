package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.constants.ReimbursementConstants;
import com.info.xiaotingtingBackEnd.constants.TeamConstants;
import com.info.xiaotingtingBackEnd.model.DayReport;
import com.info.xiaotingtingBackEnd.model.Reimbursement;
import com.info.xiaotingtingBackEnd.model.TeamRelation;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.DayReportRep;
import com.info.xiaotingtingBackEnd.repository.ReimbursementRep;
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
public class DayReportService extends BaseService<DayReport, String, DayReportRep> {
    @Override
    public DayReportRep getRepo() {
        return dayReportRep;
    }

    public void submitDayReport(String userId, String userNickname, String userAvatar, String teamId, String finishedWork, String unfinishedWork, String needCoordinatedWork, String remarks, String photo) throws PlatformException {
        if (DataCheckUtil.isEmpty(teamId))
            throw new PlatformException(-1, "必须选择团队");
        if (DataCheckUtil.isEmpty(finishedWork))
            throw new PlatformException(-1, "今日完成工作不能为空");
        TeamRelation teamRelation = teamRelationRep.findOne(new TeamRelation.TeamRelationId(teamId, userId));
        if (teamRelation == null)
            throw new PlatformException(-1, "你不属于该团队");
        DayReport dayReport = new DayReport();
        dayReport.setUserId(userId);
        dayReport.setUserNickname(userNickname);
        dayReport.setUserAvatar(userAvatar);
        dayReport.setTeamId(teamId);
        dayReport.setFinishedWork(finishedWork);
        dayReport.setUnfinishedWork(unfinishedWork);
        dayReport.setNeedCoordinatedWork(needCoordinatedWork);
        dayReport.setPhoto(photo);
        dayReport.setRemarks(remarks);
        dayReport.setCommitTime(new Date());
        dayReportRep.save(dayReport);
    }
}
