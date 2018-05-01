package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.constants.UseGoodConstants;
import com.info.xiaotingtingBackEnd.constants.WorkOffConstants;
import com.info.xiaotingtingBackEnd.model.UseGood;
import com.info.xiaotingtingBackEnd.model.WorkOff;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.base.SearchBean;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.TeamService;
import com.info.xiaotingtingBackEnd.service.UseGoodService;
import com.info.xiaotingtingBackEnd.util.DataCheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/2 17:47:27
 * Description：物品领用
 * Email: xiaoting233zhang@126.com
 */
@RestController
@RequestMapping("good")
public class UseGoodController {

    @Autowired
    UseGoodService useGoodService;

    @Autowired
    TeamService teamService;

    /**
     * 申请领用物品
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "applyUseGood", method = RequestMethod.POST)
    public ApiResponse<Object> applyUseGood(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        /*团队id*/
        String teamId = params.get("teamId");
        /*用户昵称*/
        String userNickname = params.get("userNickname");
        /*用户头像*/
        String userAvatar = params.get("userAvatar");
        /*物品用途*/
        String goodPurpose = params.get("goodPurpose");
        /*物品名称*/
        String goodName = params.get("goodName");
         /*物品数量*/
        String goodCount = params.get("goodCount");
         /*使用详情*/
        String useDetails = params.get("useDetails");
        /*图片*/
        String photo = params.get("photo");
        useGoodService.applyUseGood(userId, userNickname, userAvatar, teamId, goodPurpose, goodName, goodCount, useDetails, photo);
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
    @RequestMapping(value = "returnUseGood", method = RequestMethod.POST)
    public ApiResponse<UseGood> returnUseGood(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String useGoodId = params.get("useGoodId");
        String handleReason = params.get("handleReason");
        UseGood useGood = useGoodService.returnUseGood(userId, useGoodId, handleReason);
        ApiResponse response = new ApiResponse<>(HttpResponseCodes.SUCCESS, "回收物品领用申请成功");
        response.setData(useGood);
        return response;
    }

    /**
     * 处理物品领用申请（只要还没处理）
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "handleUseGood", method = RequestMethod.POST)
    public ApiResponse<WorkOff> handleUseGood(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String nickname = params.get("nickname");
        String avatar = params.get("avatar");
        String useGoodId = params.get("useGoodId");
        String handleReason = params.get("handleReason");
        String handleStatus = params.get("handleStatus");
        UseGood useGood = useGoodService.handleUseGood(userId, nickname, avatar, useGoodId, handleReason, Integer.parseInt(handleStatus));
        ApiResponse response = new ApiResponse<>(HttpResponseCodes.SUCCESS, "处理物品领用申请成功");
        response.setData(useGood);
        return response;
    }

    /**
     * 获取个人物品领用列表
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "getUseGoods", method = RequestMethod.POST)
    public ApiResponse<List<UseGood>> getUseGoods(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String teamId = params.get("teamId");
        String status = params.get("status");
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSearchCondition(params);
        searchCondition.addSearchBean("userId", userId, SearchBean.OPERATOR_EQ);
        searchCondition.addSearchBean("teamId", teamId, SearchBean.OPERATOR_EQ);
        if (!DataCheckUtil.isEmpty(status))
            searchCondition.addSearchBean("status", Integer.parseInt(status), SearchBean.OPERATOR_EQ);
        searchCondition.addSortBean("commitTime", "asc", SearchBean.OPERATOR_SORT);
        ApiResponse<List<UseGood>> response = useGoodService.getPageBySearchCondition(searchCondition);
        return response;
    }

    /**
     * 获取团队物品领用申请列表
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "getUseGoodsForTeam", method = RequestMethod.POST)
    public ApiResponse<List<UseGood>> getUseGoodsForTeam(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
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
            if (temp == UseGoodConstants.STATUS_USE_GOOD_RETURN)
                throw new PlatformException(-1, "查看状态不正确");
            searchCondition.addSearchBean("status", Integer.parseInt(status), SearchBean.OPERATOR_EQ);
        } else {
            searchCondition.addSearchBean("status", WorkOffConstants.STATUS_WORK_OFF_RETURN, SearchBean.OPERATOR_NE);
        }
        searchCondition.addSortBean("commitTime", "asc", SearchBean.OPERATOR_SORT);
        ApiResponse<List<UseGood>> response = useGoodService.getPageBySearchCondition(searchCondition);
        return response;
    }

    /**
     * 删除物品领用申请（回收的以及处理完后请假结束时间已经过去一个月则可以删除）
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "delUseGood", method = RequestMethod.POST)
    public ApiResponse<Object> delUseGood(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String useGoodId = params.get("useGoodId");
        useGoodService.delUseGood(userId, useGoodId);
        ApiResponse response = new ApiResponse<>(HttpResponseCodes.SUCCESS, "删除物品领用申请成功");
        return response;
    }
}
