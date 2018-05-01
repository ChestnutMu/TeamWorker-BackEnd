package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.constants.PurchaseConstants;
import com.info.xiaotingtingBackEnd.constants.WorkOffConstants;
import com.info.xiaotingtingBackEnd.model.Purchase;
import com.info.xiaotingtingBackEnd.model.WorkOff;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.base.SearchBean;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.PurchaseService;
import com.info.xiaotingtingBackEnd.service.TeamService;
import com.info.xiaotingtingBackEnd.util.DataCheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/2 17:47:27
 * Description：采购申请
 * Email: xiaoting233zhang@126.com
 */
@RestController
@RequestMapping("purchase")
public class PurchaseController {

    @Autowired
    PurchaseService purchaseService;

    @Autowired
    TeamService teamService;

    /**
     * 申请采购物品
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "applyPurchase", method = RequestMethod.POST)
    public ApiResponse<Object> applyPurchase(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        /*团队id*/
        String teamId = params.get("teamId");
        /*用户昵称*/
        String userNickname = params.get("userNickname");
        /*用户头像*/
        String userAvatar = params.get("userAvatar");
        /*采购事由*/
        String purchaseReason = params.get("purchaseReason");
        /*物品名称*/
        String goodName = params.get("goodName");
        /*物品数量*/
        String goodCount = params.get("goodCount");
        /*物品价格*/
        String goodPrice = params.get("goodPrice");
        /*支付方式*/
        String payType = params.get("payType");
        /*备注*/
        String remarks = params.get("remarks");
        /*图片*/
        String photo = params.get("photo");
        purchaseService.applyPurchase(userId, userNickname, userAvatar, teamId, purchaseReason, goodName, goodCount, goodPrice, payType, remarks, photo);
        ApiResponse response = new ApiResponse<>(HttpResponseCodes.SUCCESS, "申请成功");
        return response;
    }

    /**
     * 回收物品领用申请（只要还没处理）
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "returnPurchase", method = RequestMethod.POST)
    public ApiResponse<Purchase> returnPurchase(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String purchaseId = params.get("purchaseId");
        String handleReason = params.get("handleReason");
        Purchase purchase = purchaseService.returnPurchase(userId, purchaseId, handleReason);
        ApiResponse response = new ApiResponse<>(HttpResponseCodes.SUCCESS, "回收采购申请成功");
        response.setData(purchase);
        return response;
    }

    /**
     * 处理采购申请（只要还没处理）
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "handlePurchase", method = RequestMethod.POST)
    public ApiResponse<WorkOff> handlePurchase(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String nickname = params.get("nickname");
        String avatar = params.get("avatar");
        String purchaseId = params.get("purchaseId");
        String handleReason = params.get("handleReason");
        String handleStatus = params.get("handleStatus");
        Purchase purchase = purchaseService.handlePurchase(userId, nickname, avatar, purchaseId, handleReason, Integer.parseInt(handleStatus));
        ApiResponse response = new ApiResponse<>(HttpResponseCodes.SUCCESS, "处理采购申请成功");
        response.setData(purchase);
        return response;
    }

    /**
     * 获取个人采购列表
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "getPurchases", method = RequestMethod.POST)
    public ApiResponse<List<Purchase>> getPurchases(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String teamId = params.get("teamId");
        String status = params.get("status");
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSearchCondition(params);
        searchCondition.addSearchBean("userId", userId, SearchBean.OPERATOR_EQ);
        searchCondition.addSearchBean("teamId", teamId, SearchBean.OPERATOR_EQ);
        if (!DataCheckUtil.isEmpty(status))
            searchCondition.addSearchBean("status", Integer.parseInt(status), SearchBean.OPERATOR_EQ);
        searchCondition.addSortBean("commitTime", "asc", SearchBean.OPERATOR_SORT);
        ApiResponse<List<Purchase>> response = purchaseService.getPageBySearchCondition(searchCondition);
        return response;
    }

    /**
     * 获取团队采购申请列表
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "getPurchasesForTeam", method = RequestMethod.POST)
    public ApiResponse<List<Purchase>> getPurchasesForTeam(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String teamId = params.get("teamId");
        String teamUserId = params.get("teamUserId");
        if (!userId.equals(teamUserId))
            teamService.checkPermission(teamId, userId, teamUserId);
        String status = params.get("status");
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSearchCondition(params);
        searchCondition.addSearchBean("teamId", teamId, SearchBean.OPERATOR_EQ);
        if (!DataCheckUtil.isEmpty(teamUserId))
            searchCondition.addSearchBean("userId", teamUserId, SearchBean.OPERATOR_EQ);
        if (!DataCheckUtil.isEmpty(status)) {
            int temp = Integer.parseInt(status);
            if (temp == PurchaseConstants.STATUS_PURCHASE_RETURN)
                throw new PlatformException(-1, "查看状态不正确");
            searchCondition.addSearchBean("status", Integer.parseInt(status), SearchBean.OPERATOR_EQ);
        } else {
            searchCondition.addSearchBean("status", WorkOffConstants.STATUS_WORK_OFF_RETURN, SearchBean.OPERATOR_NE);
        }
        searchCondition.addSortBean("commitTime", "asc", SearchBean.OPERATOR_SORT);
        ApiResponse<List<Purchase>> response = purchaseService.getPageBySearchCondition(searchCondition);
        return response;
    }

    /**
     * 删除采购申请（回收的以及处理完后请假结束时间已经过去一个月则可以删除）
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "delPurchase", method = RequestMethod.POST)
    public ApiResponse<Object> delPurchase(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String purchaseId = params.get("purchaseId");
        purchaseService.delPurchase(userId, purchaseId);
        ApiResponse response = new ApiResponse<>(HttpResponseCodes.SUCCESS, "删除采购申请成功");
        return response;
    }
}
