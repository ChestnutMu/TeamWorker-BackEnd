package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.constants.ReimbursementConstants;
import com.info.xiaotingtingBackEnd.constants.TeamConstants;
import com.info.xiaotingtingBackEnd.model.Reimbursement;
import com.info.xiaotingtingBackEnd.model.TeamRelation;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
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
public class ReimbursementService extends BaseService<Reimbursement, String, ReimbursementRep> {
    @Override
    public ReimbursementRep getRepo() {
        return reimbursementRep;
    }

    public void applyReimbursement(String userId, String userNickname , String userAvatar, String teamId, String reimbursementMoney, String reimbursementType, String reimbursementDetail, String photo) throws PlatformException {
        if (DataCheckUtil.isEmpty(teamId))
            throw new PlatformException(-1, "必须选择团队");
        if (DataCheckUtil.isEmpty(reimbursementType))
            throw new PlatformException(-1, "报销类型不能为空");
        if (DataCheckUtil.isEmpty(reimbursementMoney))
            throw new PlatformException(-1, "报销金额不能为空");
        if (DataCheckUtil.isEmpty(reimbursementDetail))
            throw new PlatformException(-1, "报销明细不能为空");
        TeamRelation teamRelation = teamRelationRep.findOne(new TeamRelation.TeamRelationId(teamId, userId));
        if (teamRelation == null)
            throw new PlatformException(-1, "你不属于该团队");
        Reimbursement reimbursement = new Reimbursement();
        reimbursement.setUserId(userId);
        reimbursement.setUserNickname(userNickname);
        reimbursement.setUserAvatar(userAvatar);
        reimbursement.setTeamId(teamId);
        reimbursement.setReimbursementMoney(reimbursementMoney);
        reimbursement.setReimbursementType(reimbursementType);
        reimbursement.setReimbursementDetail(reimbursementDetail);
        reimbursement.setPhoto(photo);
        reimbursement.setStatus(ReimbursementConstants.STATUS_REIMBURSEMENT_WAITING);
        reimbursement.setCommitTime(new Date());
        reimbursementRep.save(reimbursement);
    }

    public Reimbursement returnReimbursement(String userId, String reimbursementId, String handleReason) throws PlatformException {
        Reimbursement reimbursement = reimbursementRep.findOne(reimbursementId);
        if (reimbursement == null)
            throw new PlatformException(-1, "报销申请不存在");
        if (!reimbursement.getUserId().equals(userId))
            throw new PlatformException(-1, "报销申请不属于你");
        if (reimbursement.getStatus() == ReimbursementConstants.STATUS_REIMBURSEMENT_RETURN)
            throw new PlatformException(-1, "报销申请已回收");
        if (reimbursement.getStatus() != ReimbursementConstants.STATUS_REIMBURSEMENT_WAITING)
            throw new PlatformException(-1, "报销申请已被处理");
        reimbursement.setStatus(ReimbursementConstants.STATUS_REIMBURSEMENT_RETURN);
        reimbursement.setHandleTime(new Date());
        reimbursement.setHandleReason(handleReason);
        return reimbursementRep.save(reimbursement);
    }

    public Reimbursement handleReimbursementId(String userId, String nickname, String avatar, String reimbursementId, String handleReason, int handleStatus) throws PlatformException {
        Reimbursement reimbursement = reimbursementRep.findOne(reimbursementId);
        if (reimbursement == null)
            throw new PlatformException(-1, "报销申请不存在");
        if (reimbursement.getUserId().equals(userId))
            throw new PlatformException(-1, "不能处理自己的报销申请");
        TeamRelation teamRelation = teamRelationRep.findOne(new TeamRelation.TeamRelationId(reimbursement.getTeamId(), userId));
        if (teamRelation.getType() != TeamConstants.TYPE_TEAM_ADMIN && teamRelation.getType() != TeamConstants.TYPE_TEAM_OWNER)
            throw new PlatformException(-1, "权限不足");
        if (reimbursement.getStatus() != ReimbursementConstants.STATUS_REIMBURSEMENT_WAITING)
            throw new PlatformException(-1, "报销申请已被处理");
        if (handleStatus != ReimbursementConstants.STATUS_REIMBURSEMENT_PASS && handleStatus != ReimbursementConstants.STATUS_REIMBURSEMENT_UNPASS)
            throw new PlatformException(-1, "处理状态不对");
        reimbursement.setAdminId(userId);
        reimbursement.setAdminNickname(nickname);
        reimbursement.setAdminAvatar(avatar);
        reimbursement.setStatus(handleStatus);
        reimbursement.setHandleReason(handleReason);
        reimbursement.setHandleTime(new Date());
        return reimbursementRep.save(reimbursement);
    }

    public void delReimbursement(String userId, String reimbursementId) throws PlatformException {
        Reimbursement reimbursement = reimbursementRep.findOne(reimbursementId);
        if (reimbursement == null)
            throw new PlatformException(-1, "报销申请不存在");
        if (!reimbursement.getUserId().equals(userId))
            throw new PlatformException(-1, "报销申请不属于你");
        if (reimbursement.getStatus() == ReimbursementConstants.STATUS_REIMBURSEMENT_RETURN) {
            reimbursementRep.delete(reimbursement);
            return;
        }
        if (reimbursement.getStatus() == ReimbursementConstants.STATUS_REIMBURSEMENT_WAITING)
            throw new PlatformException(-1, "报销申请还在等待处理，不能删除");
        long passTime = System.currentTimeMillis() - reimbursement.getHandleTime().getTime();
        if (passTime > 30 * 86400 * 1000L) {
            reimbursementRep.delete(reimbursement);
            return;
        } else {
            throw new PlatformException(-1, "报销申请必须结束后一个月才能删除");
        }

    }
}
