package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.constants.ReimbursementConstants;
import com.info.xiaotingtingBackEnd.model.Reimbursement;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.base.SearchBean;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.ReimbursementService;
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
 * Description：报销
 * Email: xiaoting233zhang@126.com
 */
@RestController
@RequestMapping("reimbursement")
public class ReimbursementController {

    @Autowired
    ReimbursementService reimbursementService;

    @Autowired
    TeamService teamService;

    /**
     * 申请报销
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "applyReimbursement", method = RequestMethod.POST)
    public ApiResponse<Object> applyReimbursement(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        /*团队id*/
        String teamId = params.get("teamId");
        /*用户昵称*/
        String userNickname = params.get("userNickname");
        /*用户头像*/
        String userAvatar = params.get("userAvatar");
        /*报销金额*/
        String reimbursementMoney = params.get("reimbursementMoney");
        /*报销类型*/
        String reimbursementType = params.get("reimbursementType");
        /*报销明细*/
        String reimbursementDetail = params.get("reimbursementDetail");
        /*图片*/
        String photo = params.get("photo");
        reimbursementService.applyReimbursement(userId, userNickname, userAvatar, teamId, reimbursementMoney, reimbursementType,reimbursementDetail, photo);
        ApiResponse response = new ApiResponse<>(HttpResponseCodes.SUCCESS, "申请成功");
        return response;
    }

    /**
     * 回收报销申请（只要还没处理）
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "returnReimbursement", method = RequestMethod.POST)
    public ApiResponse<Reimbursement> returnReimbursement(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String reimbursementId = params.get("reimbursementId");
        String handleReason = params.get("handleReason");
        Reimbursement reimbursement = reimbursementService.returnReimbursement(userId, reimbursementId, handleReason);
        ApiResponse response = new ApiResponse<>(HttpResponseCodes.SUCCESS, "回收报销申请成功");
        response.setData(reimbursement);
        return response;
    }

    /**
     * 处理报销申请（只要还没处理）
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "handleReimbursement", method = RequestMethod.POST)
    public ApiResponse<Reimbursement> handleReimbursement(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String nickname = params.get("nickname");
        String avatar = params.get("avatar");
        String reimbursementId = params.get("reimbursementId");
        String handleReason = params.get("handleReason");
        String handleStatus = params.get("handleStatus");
        Reimbursement reimbursement = reimbursementService.handleReimbursementId(userId,nickname,avatar, reimbursementId, handleReason, Integer.parseInt(handleStatus));
        ApiResponse response = new ApiResponse<>(HttpResponseCodes.SUCCESS, "处理请假条成功");
        response.setData(reimbursement);
        return response;
    }

    /**
     * 获取个人报销申请列表
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "getReimbursements", method = RequestMethod.POST)
    public ApiResponse<List<Reimbursement>> getReimbursements(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String teamId = params.get("teamId");
        String status = params.get("status");
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSearchCondition(params);
        searchCondition.addSearchBean("userId", userId, SearchBean.OPERATOR_EQ);
        searchCondition.addSearchBean("teamId", teamId, SearchBean.OPERATOR_EQ);
        if (!DataCheckUtil.isEmpty(status))
            searchCondition.addSearchBean("status", Integer.parseInt(status), SearchBean.OPERATOR_EQ);
        searchCondition.addSortBean("commitTime", "asc", SearchBean.OPERATOR_SORT);
        ApiResponse<List<Reimbursement>> response = reimbursementService.getPageBySearchCondition(searchCondition);
        return response;
    }

    /**
     * 获取报销申请列表
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "getReimbursementsForTeam", method = RequestMethod.POST)
    public ApiResponse<List<Reimbursement>> getReimbursementsForTeam(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
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
            if (temp == ReimbursementConstants.STATUS_REIMBURSEMENT_RETURN)
                throw new PlatformException(-1, "查看状态不正确");
            searchCondition.addSearchBean("status", Integer.parseInt(status), SearchBean.OPERATOR_EQ);
        } else {
            searchCondition.addSearchBean("status", ReimbursementConstants.STATUS_REIMBURSEMENT_RETURN, SearchBean.OPERATOR_NE);
        }
        searchCondition.addSortBean("commitTime", "asc", SearchBean.OPERATOR_SORT);
        ApiResponse<List<Reimbursement>> response = reimbursementService.getPageBySearchCondition(searchCondition);
        return response;
    }


    /**
     * 删除报销申请（回收的以及处理完后报销申请已经过去一个月则可以删除）
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "delReimbursement", method = RequestMethod.POST)
    public ApiResponse<Object> delReimbursement(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String reimbursementId = params.get("reimbursementId");
        reimbursementService.delReimbursement(userId, reimbursementId);
        ApiResponse response = new ApiResponse<>(HttpResponseCodes.SUCCESS, "处理请假条成功");
        return response;
    }
}
