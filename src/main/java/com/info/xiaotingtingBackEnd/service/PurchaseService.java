package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.constants.PurchaseConstants;
import com.info.xiaotingtingBackEnd.constants.TeamConstants;
import com.info.xiaotingtingBackEnd.constants.PurchaseConstants;
import com.info.xiaotingtingBackEnd.model.Purchase;
import com.info.xiaotingtingBackEnd.model.TeamRelation;
import com.info.xiaotingtingBackEnd.model.Purchase;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.PurchaseRep;
import com.info.xiaotingtingBackEnd.repository.PurchaseRep;
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
public class PurchaseService extends BaseService<Purchase, String, PurchaseRep> {
    @Override
    public PurchaseRep getRepo() {
        return purchaseRep;
    }

    public void applyPurchase(String userId, String userNickname, String userAvatar, String teamId, String purchaseReason, String goodName, String goodCount, String goodPrice, String payType, String remarks, String photo) throws PlatformException {
        if (DataCheckUtil.isEmpty(teamId))
            throw new PlatformException(-1, "必须选择团队");
        if (DataCheckUtil.isEmpty(purchaseReason))
            throw new PlatformException(-1, "采购事由不能为空");
        if (DataCheckUtil.isEmpty(goodName))
            throw new PlatformException(-1, "物品名称不能为空");
        if (DataCheckUtil.isEmpty(goodCount))
            throw new PlatformException(-1, "物品数量不能为空");
        if (DataCheckUtil.isEmpty(goodPrice))
            throw new PlatformException(-1, "物品价格不能为空");
        TeamRelation teamRelation = teamRelationRep.findOne(new TeamRelation.TeamRelationId(teamId, userId));
        if (teamRelation == null)
            throw new PlatformException(-1, "你不属于该团队");
        Purchase purchase = new Purchase();
        purchase.setUserId(userId);
        purchase.setUserNickname(userNickname);
        purchase.setUserAvatar(userAvatar);
        purchase.setTeamId(teamId);
        purchase.setPurchaseReason(purchaseReason);
        purchase.setGoodName(goodName);
        purchase.setGoodCount(goodCount);
        purchase.setGoodPrice(goodPrice);
        purchase.setPayType(payType);
        purchase.setRemarks(remarks);
        purchase.setPhoto(photo);
        purchase.setStatus(PurchaseConstants.STATUS_PURCHASE_WAITING);
        purchase.setCommitTime(new Date());
        purchaseRep.save(purchase);
    }

    public Purchase returnPurchase(String userId, String purchaseId, String handleReason) throws PlatformException {
        Purchase purchase = purchaseRep.findOne(purchaseId);
        if (purchase == null)
            throw new PlatformException(-1, "采购申请不存在");
        if (!purchase.getUserId().equals(userId))
            throw new PlatformException(-1, "采购申请不属于你");
        if (purchase.getStatus() == PurchaseConstants.STATUS_PURCHASE_RETURN)
            throw new PlatformException(-1, "采购申请已回收");
        if (purchase.getStatus() != PurchaseConstants.STATUS_PURCHASE_WAITING)
            throw new PlatformException(-1, "采购申请已被处理");
        purchase.setStatus(PurchaseConstants.STATUS_PURCHASE_RETURN);
        purchase.setHandleTime(new Date());
        purchase.setHandleReason(handleReason);
        return purchaseRep.save(purchase);
    }

    public Purchase handlePurchase(String userId, String nickname, String avatar, String purchaseId, String handleReason, int handleStatus) throws PlatformException {
        Purchase purchase = purchaseRep.findOne(purchaseId);
        if (purchase == null)
            throw new PlatformException(-1, "采购申请不存在");
        if (purchase.getUserId().equals(userId))
            throw new PlatformException(-1, "不能处理自己的采购申请");
        TeamRelation teamRelation = teamRelationRep.findOne(new TeamRelation.TeamRelationId(purchase.getTeamId(), userId));
        if (teamRelation.getType() != TeamConstants.TYPE_TEAM_ADMIN && teamRelation.getType() != TeamConstants.TYPE_TEAM_OWNER)
            throw new PlatformException(-1, "权限不足");
        if (purchase.getStatus() != PurchaseConstants.STATUS_PURCHASE_WAITING)
            throw new PlatformException(-1, "采购申请已被处理");
        if (handleStatus != PurchaseConstants.STATUS_PURCHASE_PASS && handleStatus != PurchaseConstants.STATUS_PURCHASE_UNPASS)
            throw new PlatformException(-1, "处理状态不对");
        purchase.setAdminId(userId);
        purchase.setAdminNickname(nickname);
        purchase.setAdminAvatar(avatar);
        purchase.setStatus(handleStatus);
        purchase.setHandleReason(handleReason);
        purchase.setHandleTime(new Date());
        return purchaseRep.save(purchase);
    }

    public void delPurchase(String userId, String purchaseId) throws PlatformException {
        Purchase purchase = purchaseRep.findOne(purchaseId);
        if (purchase == null)
            throw new PlatformException(-1, "采购申请不存在");
        if (!purchase.getUserId().equals(userId))
            throw new PlatformException(-1, "采购申请不属于你");
        if (purchase.getStatus() == PurchaseConstants.STATUS_PURCHASE_RETURN) {
            purchaseRep.delete(purchase);
            return;
        }
        if (purchase.getStatus() == PurchaseConstants.STATUS_PURCHASE_WAITING)
            throw new PlatformException(-1, "采购申请还在等待处理，不能删除");
        long passTime = System.currentTimeMillis() - purchase.getCommitTime().getTime();
        if (passTime > 30 * 86400 * 1000L) {
            purchaseRep.delete(purchase);
            return;
        } else {
            throw new PlatformException(-1, "采购申请必须结束后一个月才能删除");
        }

    }

}
