package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.constants.TeamConstants;
import com.info.xiaotingtingBackEnd.constants.UseGoodConstants;
import com.info.xiaotingtingBackEnd.model.TeamRelation;
import com.info.xiaotingtingBackEnd.model.UseGood;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.UseGoodRep;
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
public class UseGoodService extends BaseService<UseGood, String, UseGoodRep> {
    @Override
    public UseGoodRep getRepo() {
        return useGoodRep;
    }

    public void applyUseGood(String userId,String userNickname ,String userAvatar,String teamId, String goodPurpose, String goodName,String goodCount,String useDetails, String photo) throws PlatformException {
        if (DataCheckUtil.isEmpty(teamId))
            throw new PlatformException(-1, "必须选择团队");
        if (DataCheckUtil.isEmpty(goodPurpose))
            throw new PlatformException(-1, "物品用途不能为空");
        if (DataCheckUtil.isEmpty(goodName))
            throw new PlatformException(-1, "物品名称不能为空");
        TeamRelation teamRelation = teamRelationRep.findOne(new TeamRelation.TeamRelationId(teamId, userId));
        if (teamRelation == null)
            throw new PlatformException(-1, "你不属于该团队");
        UseGood useGood = new UseGood();
        useGood.setUserId(userId);
        useGood.setUserNickname(userNickname);
        useGood.setUserAvatar(userAvatar);
        useGood.setTeamId(teamId);
        useGood.setGoodPurpose(goodPurpose);
        useGood.setGoodName(goodName);
        useGood.setGoodCount(goodCount);
        useGood.setUseDetails(useDetails);
        useGood.setPhoto(photo);
        useGood.setStatus(UseGoodConstants.STATUS_USE_GOOD_WAITING);
        useGood.setCommitTime(new Date());
        useGoodRep.save(useGood);
    }

    public UseGood returnUseGood(String userId, String useGoodId, String handleReason) throws PlatformException {
        UseGood useGood = useGoodRep.findOne(useGoodId);
        if (useGood == null)
            throw new PlatformException(-1, "物品领用申请不存在");
        if (!useGood.getUserId().equals(userId))
            throw new PlatformException(-1, "物品领用申请不属于你");
        if (useGood.getStatus() == UseGoodConstants.STATUS_USE_GOOD_RETURN)
            throw new PlatformException(-1, "物品领用申请已回收");
        if (useGood.getStatus() != UseGoodConstants.STATUS_USE_GOOD_WAITING)
            throw new PlatformException(-1, "物品领用申请已被处理");
        useGood.setStatus(UseGoodConstants.STATUS_USE_GOOD_RETURN);
        useGood.setHandleTime(new Date());
        useGood.setHandleReason(handleReason);
        return useGoodRep.save(useGood);
    }

    public UseGood handleUseGood(String userId, String nickname,String avatar,String useGoodId, String handleReason, int handleStatus) throws PlatformException {
        UseGood useGood = useGoodRep.findOne(useGoodId);
        if (useGood == null)
            throw new PlatformException(-1, "物品领用申请不存在");
        if (useGood.getUserId().equals(userId))
            throw new PlatformException(-1, "不能处理自己的物品领用申请");
        TeamRelation teamRelation = teamRelationRep.findOne(new TeamRelation.TeamRelationId(useGood.getTeamId(), userId));
        if (teamRelation.getType() != TeamConstants.TYPE_TEAM_ADMIN && teamRelation.getType() != TeamConstants.TYPE_TEAM_OWNER)
            throw new PlatformException(-1, "权限不足");
        if (useGood.getStatus() != UseGoodConstants.STATUS_USE_GOOD_WAITING)
            throw new PlatformException(-1, "物品领用申请已被处理");
        if (handleStatus != UseGoodConstants.STATUS_USE_GOOD_PASS && handleStatus != UseGoodConstants.STATUS_USE_GOOD_UNPASS)
            throw new PlatformException(-1, "处理状态不对");
        useGood.setAdminId(userId);
        useGood.setAdminNickname(nickname);
        useGood.setAdminAvatar(avatar);
        useGood.setStatus(handleStatus);
        useGood.setHandleReason(handleReason);
        useGood.setHandleTime(new Date());
        return useGoodRep.save(useGood);
    }

    public void delUseGood(String userId, String useGoodId) throws PlatformException {
        UseGood useGood = useGoodRep.findOne(useGoodId);
        if (useGood == null)
            throw new PlatformException(-1, "物品领用申请不存在");
        if (!useGood.getUserId().equals(userId))
            throw new PlatformException(-1, "物品领用申请不属于你");
        if (useGood.getStatus() == UseGoodConstants.STATUS_USE_GOOD_RETURN) {
            useGoodRep.delete(useGood);
            return;
        }
        if (useGood.getStatus() == UseGoodConstants.STATUS_USE_GOOD_WAITING)
            throw new PlatformException(-1, "物品领用申请还在等待处理，不能删除");
        long passTime = System.currentTimeMillis() - useGood.getCommitTime().getTime();
        if (passTime > 30 * 86400 * 1000L) {
            useGoodRep.delete(useGood);
            return;
        } else {
            throw new PlatformException(-1, "物品领用申请必须结束后一个月才能删除");
        }

    }

}
