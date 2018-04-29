package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.constants.WorkOffConstants;
import com.info.xiaotingtingBackEnd.model.WorkOff;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.base.SearchBean;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.TeamService;
import com.info.xiaotingtingBackEnd.service.WorkOffService;
import com.info.xiaotingtingBackEnd.util.DataCheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/2 17:47:27
 * Description：请假
 * Email: xiaoting233zhang@126.com
 */
@RestController
@RequestMapping("workoff")
public class WorkOffController {

    @Autowired
    WorkOffService workOffService;

    @Autowired
    TeamService teamService;

    //    @RequestMapping(value = "applyWorkOff", method = RequestMethod.POST)
//    public ApiResponse<WorkOff> applyWorkOff(@RequestBody WorkOff workOff) {
//        workOff.setStatus(0);
//        workOffService.saveAndSendApplication(workOff);
//        ApiResponse<WorkOff> workOffApiResponse = new ApiResponse<>();
//        workOffApiResponse.setStatus(HttpResponseCodes.SUCCESS);
//        workOffApiResponse.setMessage("您的请假申请已提交");
//        workOffApiResponse.setData(workOff);
//        return workOffApiResponse;
//    }
//
//    @RequestMapping(value = "approveWorkOff", method = RequestMethod.POST)
//    public ApiResponse approveWorkOff(@RequestBody WorkOff workOff) {
//        workOffService.save(workOff);
//        ApiResponse workOffApiResponse = new ApiResponse<>();
//        workOffApiResponse.setStatus(HttpResponseCodes.SUCCESS);
//        workOffApiResponse.setMessage("已处理该请假");
//        return workOffApiResponse;
//    }
//

    /**
     * 申请请假
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "applyWorkOff", method = RequestMethod.POST)
    public ApiResponse<Object> applyWorkOff(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        /*团队id*/
        String teamId = params.get("teamId");
        /*用户昵称*/
        String userNickname = params.get("userNickname");
        /*用户头像*/
        String userAvatar = params.get("userAvatar");
        /*类型*/
        String workOffType = params.get("workOffType");
        /*内容*/
        String workOffReason = params.get("workOffReason");
        /*图片*/
        String photo = params.get("photo");
        /*开始时间*/
        long startTime = Long.parseLong(params.get("startTime"));
        /*结束时间*/
        long endTime = Long.parseLong(params.get("endTime"));
        workOffService.applyWorkOff(userId, userNickname, userAvatar, teamId, workOffType, workOffReason, photo, startTime, endTime);
        ApiResponse response = new ApiResponse<>(HttpResponseCodes.SUCCESS, "申请成功");
        return response;
    }

    /**
     * 回收请假条（只要还没处理）
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "returnWorkOff", method = RequestMethod.POST)
    public ApiResponse<WorkOff> returnWorkOff(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String workOffId = params.get("workOffId");
        String handleReason = params.get("handleReason");
        WorkOff workOff = workOffService.returnWorkOff(userId, workOffId, handleReason);
        ApiResponse response = new ApiResponse<>(HttpResponseCodes.SUCCESS, "回收请假条成功");
        response.setData(workOff);
        return response;
    }

    /**
     * 处理请假条（只要还没处理）
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "handleWorkOff", method = RequestMethod.POST)
    public ApiResponse<WorkOff> handleWorkOff(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String nickname = params.get("nickname");
        String avatar = params.get("avatar");
        String workOffId = params.get("workOffId");
        String handleReason = params.get("handleReason");
        String handleStatus = params.get("handleStatus");
        WorkOff workOff = workOffService.handleWorkOff(userId,nickname,avatar, workOffId, handleReason, Integer.parseInt(handleStatus));
        ApiResponse response = new ApiResponse<>(HttpResponseCodes.SUCCESS, "处理请假条成功");
        response.setData(workOff);
        return response;
    }

    /**
     * 获取个人请假条列表
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "getWorkOffs", method = RequestMethod.POST)
    public ApiResponse<List<WorkOff>> getWorkOffs(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String teamId = params.get("teamId");
        String status = params.get("status");
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSearchCondition(params);
        searchCondition.addSearchBean("userId", userId, SearchBean.OPERATOR_EQ);
        searchCondition.addSearchBean("teamId", teamId, SearchBean.OPERATOR_EQ);
        if (!DataCheckUtil.isEmpty(status))
            searchCondition.addSearchBean("status", Integer.parseInt(status), SearchBean.OPERATOR_EQ);
        searchCondition.addSortBean("commitTime", "asc", SearchBean.OPERATOR_SORT);
        ApiResponse<List<WorkOff>> response = workOffService.getPageBySearchCondition(searchCondition);
        return response;
    }

    /**
     * 获取团队请假条列表
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "getWorkOffsForTeam", method = RequestMethod.POST)
    public ApiResponse<List<WorkOff>> getWorkOffsForTeam(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
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
            if (temp == WorkOffConstants.STATUS_WORK_OFF_RETURN)
                throw new PlatformException(-1, "查看状态不正确");
            searchCondition.addSearchBean("status", Integer.parseInt(status), SearchBean.OPERATOR_EQ);
        } else {
            searchCondition.addSearchBean("status", WorkOffConstants.STATUS_WORK_OFF_RETURN, SearchBean.OPERATOR_NE);
        }
        searchCondition.addSortBean("commitTime", "asc", SearchBean.OPERATOR_SORT);
        ApiResponse<List<WorkOff>> response = workOffService.getPageBySearchCondition(searchCondition);
        return response;
    }


    /**
     * 删除请假条（回收的以及处理完后请假结束时间已经过去一个月则可以删除）
     *
     * @param userId
     * @param params
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "delWorkOff", method = RequestMethod.POST)
    public ApiResponse<Object> delWorkOff(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String workOffId = params.get("workOffId");
        workOffService.delWorkOff(userId, workOffId);
        ApiResponse response = new ApiResponse<>(HttpResponseCodes.SUCCESS, "处理请假条成功");
        return response;
    }
}
