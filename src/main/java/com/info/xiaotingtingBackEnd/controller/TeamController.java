package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.DepartmentMemberRelation;
import com.info.xiaotingtingBackEnd.model.Team;
import com.info.xiaotingtingBackEnd.model.vo.TeamVo;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/8 21:58:00
 * Description：
 * Email: xiaoting233zhang@126.com
 */

@RestController
@RequestMapping("department")
public class TeamController {

    @Autowired
    DepartmentService departmentService;

    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "buildTeam", method = RequestMethod.POST)
    public ApiResponse<Team> buildTeam(@RequestBody TeamVo teamVo) {
        ApiResponse<Team> apiResponse = new ApiResponse<>();
        Team team = departmentService.addTeam(teamVo.getTeam());
        if (team != null) {
            List<DepartmentMemberRelation> relationList = new ArrayList<>(teamVo.getUserIds().size());
            for (String userId : teamVo.getUserIds()) {
                relationList.add(new DepartmentMemberRelation(team.getTeamId(), userId));
            }
            departmentService.addDepartmentMemberRelations(relationList);
            apiResponse.setData(team);
            apiResponse.setStatus(HttpResponseCodes.SUCCESS);
        } else {
            apiResponse.setStatus(HttpResponseCodes.FAILED);
            apiResponse.setMessage("团队已存在");
        }
        return apiResponse;
    }

    @RequestMapping(value = "getMyTeams", method = RequestMethod.POST)
    public ApiResponse<List<Team>> getMyTeams(@RequestHeader("uid") String userId) {
        return departmentService.getTeamByUserId(userId);
    }
}
