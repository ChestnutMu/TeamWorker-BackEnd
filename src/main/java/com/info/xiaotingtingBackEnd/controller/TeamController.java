package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.Team;
import com.info.xiaotingtingBackEnd.model.TeamRelation;
import com.info.xiaotingtingBackEnd.model.User;
import com.info.xiaotingtingBackEnd.model.vo.TeamUserVo;
import com.info.xiaotingtingBackEnd.model.vo.TeamVo;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.service.DepartmentService;
import com.info.xiaotingtingBackEnd.service.TeamService;
import com.info.xiaotingtingBackEnd.util.DataCheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/8 21:58:00
 * Description：
 * Email: xiaoting233zhang@126.com
 */

@RestController
@RequestMapping("team")
public class TeamController {

    @Autowired
    TeamService teamService;

    @Autowired
    DepartmentService departmentService;

    /**
     * 创建团队
     *
     * @param userId
     * @param teamVo
     * @return
     * @throws PlatformException
     */
    @RequestMapping(value = "buildTeam", method = RequestMethod.POST)
    public ApiResponse<Team> buildTeam(@RequestHeader("uid") String userId, @RequestBody TeamVo teamVo) throws PlatformException {
        ApiResponse<Team> apiResponse = new ApiResponse<>();
        Team result = teamService.buildTeam(userId, teamVo.getTeam(), teamVo.getUserList());
        apiResponse.setData(result);
        return apiResponse;
    }

    @RequestMapping(value = "updateTeamInformation", method = RequestMethod.POST)
    public ApiResponse<Team> updateTeamInformation(@RequestHeader("uid") String userId, @RequestBody Team team) throws PlatformException {
        ApiResponse<Team> apiResponse = new ApiResponse<>();
        Team result = teamService.updateTeamInformation(userId, team);
        apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        apiResponse.setMessage("修改成功");
        apiResponse.setData(result);
        return apiResponse;
    }

    /**
     * 获取个人团队
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "getMyTeams", method = RequestMethod.POST)
    public ApiResponse<List<Team>> getMyTeams(@RequestHeader("uid") String userId) {
        ApiResponse<List<Team>> apiResponse = new ApiResponse<>();
        List<Team> teamList = teamService.getTeamsByUserId(userId);
        apiResponse.setData(teamList);
        return apiResponse;
    }

    /**
     * 获取个人与团队关系
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "getTeamRelation", method = RequestMethod.POST)
    public ApiResponse<TeamRelation> getTeamRelation(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) {
        String teamId = params.get("teamId");
        ApiResponse<TeamRelation> apiResponse = new ApiResponse<>();
        TeamRelation teamRelation = teamService.getTeamRelation(userId, teamId);
        apiResponse.setData(teamRelation);
        return apiResponse;
    }

    /**
     * 获取团队人员
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "getTeamers", method = RequestMethod.POST)
    public ApiResponse<List<TeamUserVo>> getTeamers(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String teamId = params.get("teamId");
        ApiResponse<List<TeamUserVo>> apiResponse = new ApiResponse<>();
        List<TeamUserVo> teamUserVoList = teamService.getTeamers(userId, teamId);
        apiResponse.setData(teamUserVoList);
        return apiResponse;
    }

    /**
     * 添加团队人员（拥有者和管理员）
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "addTeamUser", method = RequestMethod.POST)
    public ApiResponse<Object> addTeamUser(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String teamId = params.get("teamId");
        String teamUserId = params.get("teamUserId");
        ApiResponse<Object> apiResponse = new ApiResponse<>(0, "添加成功");
        teamService.addTeamUser(userId, teamId, teamUserId);
        return apiResponse;
    }

    /**
     * 删除团队人员（拥有者和管理员）
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "delTeamUser", method = RequestMethod.POST)
    public ApiResponse<Object> delTeamUser(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String teamId = params.get("teamId");
        String teamUserId = params.get("teamUserId");
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        teamService.delTeamUser(userId, teamId, teamUserId);
        return apiResponse;
    }

    /**
     * 给予或解除团队人员管理员权限
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "authTeamUser", method = RequestMethod.POST)
    public ApiResponse<Object> authTeamUser(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String teamId = params.get("teamId");
        String teamUserId = params.get("teamUserId");
        String type = params.get("type");
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        teamService.authTeamUser(userId, teamId, teamUserId, Integer.parseInt(type));
        return apiResponse;
    }

    /**
     * 转让团队（拥有者）
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "giveUpTeamOwner", method = RequestMethod.POST)
    public ApiResponse<Object> giveUpTeamOwner(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String teamId = params.get("teamId");
        String teamUserId = params.get("teamUserId");
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        teamService.giveUpTeamOwner(userId, teamId, teamUserId);
        return apiResponse;
    }

    /**
     * 解散团队（拥有者）
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "releaseTeam", method = RequestMethod.POST)
    public ApiResponse<Object> releaseTeam(@RequestHeader("uid") String userId, @RequestBody Map<String, String> params) throws PlatformException {
        String teamId = params.get("teamId");
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        teamService.releaseTeam(userId, teamId);
        return apiResponse;
    }


}
