package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.constants.TeamConstants;
import com.info.xiaotingtingBackEnd.constants.WorkOffConstants;
import com.info.xiaotingtingBackEnd.model.TeamRelation;
import com.info.xiaotingtingBackEnd.model.WorkOff;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.WorkOffRep;
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
public class WorkOffService extends BaseService<WorkOff, String, WorkOffRep> {
    @Override
    public WorkOffRep getRepo() {
        return workOffRep;
    }

    public void applyWorkOff(String userId,String userNickname ,String userAvatar,String teamId, String workOffType, String workOffReason, String photo, long startTime, long endTime) throws PlatformException {
        if (DataCheckUtil.isEmpty(teamId))
            throw new PlatformException(-1, "必须选择团队");
        if (DataCheckUtil.isEmpty(workOffType))
            throw new PlatformException(-1, "请假类型不能为空");
        if (DataCheckUtil.isEmpty(workOffReason))
            throw new PlatformException(-1, "请假内容不能为空");
        if (startTime >= endTime)
            throw new PlatformException(-1, "请假时间有误");
        TeamRelation teamRelation = teamRelationRep.findOne(new TeamRelation.TeamRelationId(teamId, userId));
        if (teamRelation == null)
            throw new PlatformException(-1, "你不属于该团队");
        WorkOff workOff = new WorkOff();
        workOff.setUserId(userId);
        workOff.setUserNickname(userNickname);
        workOff.setUserAvatar(userAvatar);
        workOff.setTeamId(teamId);
        workOff.setWorkOffType(workOffType);
        workOff.setWorkOffReason(workOffReason);
        workOff.setPhoto(photo);
        workOff.setStartTime(new Date(startTime));
        workOff.setEndTime(new Date(endTime));
        workOff.setStatus(WorkOffConstants.STATUS_WORK_OFF_WAITING);
        workOff.setCommitTime(new Date());
        workOffRep.save(workOff);
    }

    public WorkOff returnWorkOff(String userId, String workOffId, String handleReason) throws PlatformException {
        WorkOff workOff = workOffRep.findOne(workOffId);
        if (workOff == null)
            throw new PlatformException(-1, "请求条不存在");
        if (!workOff.getUserId().equals(userId))
            throw new PlatformException(-1, "请求条不属于你");
        if (workOff.getStatus() == WorkOffConstants.STATUS_WORK_OFF_RETURN)
            throw new PlatformException(-1, "请求条已回收");
        if (workOff.getStatus() != WorkOffConstants.STATUS_WORK_OFF_WAITING)
            throw new PlatformException(-1, "请求条已被处理");
        workOff.setStatus(WorkOffConstants.STATUS_WORK_OFF_RETURN);
        workOff.setHandleTime(new Date());
        workOff.setHandleReason(handleReason);
        return workOffRep.save(workOff);
    }

    public WorkOff handleWorkOff(String userId, String nickname,String avatar,String workOffId, String handleReason, int handleStatus) throws PlatformException {
        WorkOff workOff = workOffRep.findOne(workOffId);
        if (workOff == null)
            throw new PlatformException(-1, "请求条不存在");
        if (workOff.getUserId().equals(userId))
            throw new PlatformException(-1, "不能处理自己的请假条");
        TeamRelation teamRelation = teamRelationRep.findOne(new TeamRelation.TeamRelationId(workOff.getTeamId(), userId));
        if (teamRelation.getType() != TeamConstants.TYPE_TEAM_ADMIN && teamRelation.getType() != TeamConstants.TYPE_TEAM_OWNER)
            throw new PlatformException(-1, "权限不足");
        if (workOff.getStatus() != WorkOffConstants.STATUS_WORK_OFF_WAITING)
            throw new PlatformException(-1, "请假条已被处理");
        if (handleStatus != WorkOffConstants.STATUS_WORK_OFF_PASS && handleStatus != WorkOffConstants.STATUS_WORK_OFF_UNPASS)
            throw new PlatformException(-1, "处理状态不对");
        workOff.setAdminId(userId);
        workOff.setAdminNickname(nickname);
        workOff.setAdminAvatar(avatar);
        workOff.setStatus(handleStatus);
        workOff.setHandleReason(handleReason);
        workOff.setHandleTime(new Date());
        return workOffRep.save(workOff);
    }

    public void delWorkOff(String userId, String workOffId) throws PlatformException {
        WorkOff workOff = workOffRep.findOne(workOffId);
        if (workOff == null)
            throw new PlatformException(-1, "请求条不存在");
        if (!workOff.getUserId().equals(userId))
            throw new PlatformException(-1, "请求条不属于你");
        if (workOff.getStatus() == WorkOffConstants.STATUS_WORK_OFF_RETURN) {
            workOffRep.delete(workOff);
            return;
        }
        if (workOff.getStatus() == WorkOffConstants.STATUS_WORK_OFF_WAITING)
            throw new PlatformException(-1, "请求条还在等待处理，不能删除");
        long passTime = System.currentTimeMillis() - workOff.getEndTime().getTime();
        if (passTime > 30 * 86400 * 1000L) {
            workOffRep.delete(workOff);
            return;
        } else {
            throw new PlatformException(-1, "请求条必须结束后一个月才能删除");
        }

    }

//    public void saveAndSendApplication(WorkOff workOff) {
//        workOffRep.save(workOff);
//
//        Message message = new Message();
//        message.setChatId(EntityUtil.getIdByTimeStampAndRandom());
//        message.setMessageType(1);
//        message.setSend(false);
//        message.setRead(false);
//        message.setSenderId(workOff.getUserId());
//        message.setReceiverId(workOff.getApproverId());
//        message.setChatName("工作通知");
//        message.setContent(workOff.getUserName() + "的请假需要您的审批");
//        message.setTime(new Date());
//        handler.sendMessage(message);
//    }
//
//    public ApiResponse approveWorkOff(WorkOff workOff) {
//        workOffRep.save(workOff);
//        ApiResponse workOffApiResponse = new ApiResponse<>();
//        workOffApiResponse.setStatus(HttpResponseCodes.SUCCESS);
//        workOffApiResponse.setMessage("已处理该请假");
//        return workOffApiResponse;
//    }
}
