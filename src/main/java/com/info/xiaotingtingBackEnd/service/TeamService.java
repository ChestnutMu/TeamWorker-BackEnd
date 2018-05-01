package com.info.xiaotingtingBackEnd.service;

import com.google.gson.reflect.TypeToken;
import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.constants.TeamConstants;
import com.info.xiaotingtingBackEnd.model.Team;
import com.info.xiaotingtingBackEnd.model.TeamRelation;
import com.info.xiaotingtingBackEnd.model.User;
import com.info.xiaotingtingBackEnd.model.vo.TeamUserVo;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.pojo.PlatformException;
import com.info.xiaotingtingBackEnd.repository.TeamRep;
import com.info.xiaotingtingBackEnd.service.base.BaseService;
import com.info.xiaotingtingBackEnd.util.DataCheckUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/10 15:23:00
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Service
public class TeamService extends BaseService<Team, String, TeamRep> {

    @Override
    public TeamRep getRepo() {
        return teamRep;
    }

    @Transactional(rollbackFor = Exception.class)
    public Team buildTeam(String userId, Team team, String userJson) throws PlatformException {
        if (DataCheckUtil.isEmpty(team.getTeamName()))
            throw new PlatformException(-1, "团队名字不能为空");
        if (DataCheckUtil.isEmpty(team.getTeamIndustry()))
            throw new PlatformException(-1, "行业不能为空");
        if (DataCheckUtil.isEmpty(team.getTeamDesc()))
            throw new PlatformException(-1, "团队描述不能为空");
        if (DataCheckUtil.isEmpty(team.getTeamRegion()))
            throw new PlatformException(-1, "团队地区不能为空");

        Date now = new Date();

        team.setCreateTime(now);
        team.setUpdateTime(now);

        teamRep.save(team);

        List<String> userList = gson.fromJson(userJson, new TypeToken<List<String>>() {
        }.getType());

        List<TeamRelation> peoples = new ArrayList<>(userList.size() + 1);

        for (String normalId : userList) {
            TeamRelation teamRelation = new TeamRelation();
            teamRelation.setTeamId(team.getTeamId());
            teamRelation.setUserId(normalId);
            teamRelation.setType(TeamConstants.TYPE_TEAM_NORMAL);
            teamRelation.setUpdateTime(now);
            peoples.add(teamRelation);
        }

        TeamRelation teamRelation = new TeamRelation();
        teamRelation.setTeamId(team.getTeamId());
        teamRelation.setUserId(userId);
        teamRelation.setType(TeamConstants.TYPE_TEAM_OWNER);
        teamRelation.setUpdateTime(now);
        peoples.add(teamRelation);

        try {
            teamRelationRep.save(peoples);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new PlatformException(-1, "团队人员不能重复");
        }
        return team;
    }


    public List<Team> getTeamsByUserId(String userId) {
        List<Team> teamList = teamRep.getTeamByUserId(userId);
        return teamList;
    }

    public TeamRelation getTeamRelation(String userId, String teamId) {
        return teamRelationRep.findOne(new TeamRelation.TeamRelationId(teamId, userId));
    }

    public List<TeamUserVo> getTeamers(String userId, String teamId) throws PlatformException {
        TeamRelation teamRelation = getTeamRelation(userId, teamId);
        if (teamRelation == null)
            throw new PlatformException(-1, "不属于该团队");
        return teamRelationRep.getTeamers(teamId);
    }

    public void addTeamUser(String userId, String teamId, String teamUserId) throws PlatformException {
        TeamRelation teamRelation = getTeamRelation(userId, teamId);
        if (null == teamRelation)
            throw new PlatformException(-1, "不属于该团队");
        if (teamRelation.getType() != TeamConstants.TYPE_TEAM_ADMIN && teamRelation.getType() != TeamConstants.TYPE_TEAM_OWNER)
            throw new PlatformException(-1, "只有团队拥有者和管理员才能添加人员");

        TeamRelation teamRelationTemp = getTeamRelation(teamUserId, teamId);
        if (null != teamRelationTemp)
            throw new PlatformException(-1, "该用户已在团队中");

        TeamRelation addTeamRelation = new TeamRelation();
        addTeamRelation.setTeamId(teamId);
        addTeamRelation.setUserId(teamUserId);
        addTeamRelation.setType(TeamConstants.TYPE_TEAM_NORMAL);
        addTeamRelation.setUpdateTime(new Date());

        teamRelationRep.save(addTeamRelation);
    }

    public void delTeamUser(String userId, String teamId, String teamUserId) throws PlatformException {
        TeamRelation teamRelation = getTeamRelation(userId, teamId);
        if (teamRelation == null)
            throw new PlatformException(-1, "不属于该团队");
        if (teamRelation.getType() != TeamConstants.TYPE_TEAM_ADMIN && teamRelation.getType() != TeamConstants.TYPE_TEAM_OWNER)
            throw new PlatformException(-1, "只有团队拥有者和管理员才能删除人员");

        TeamRelation delTeamRelation = getTeamRelation(teamUserId, teamId);
        if (delTeamRelation == null)
            throw new PlatformException(-1, "该用户已不在团队");

        if (delTeamRelation.getType() == TeamConstants.TYPE_TEAM_OWNER)
            throw new PlatformException(-1, "不能删除团队拥有者");

        teamRelationRep.delete(delTeamRelation);
    }

    public void authTeamUser(String userId, String teamId, String teamUserId, int type) throws PlatformException {
        TeamRelation teamRelation = getTeamRelation(userId, teamId);
        if (teamRelation == null)
            throw new PlatformException(-1, "不属于该团队");
        if (teamRelation.getType() != TeamConstants.TYPE_TEAM_OWNER)
            throw new PlatformException(-1, "只有团队拥有者给予团队人员权限");

        if (type == TeamConstants.TYPE_TEAM_ADMIN) {
            TeamRelation addTeamRelation = getTeamRelation(teamUserId, teamId);
            if (addTeamRelation.getType() == TeamConstants.TYPE_TEAM_ADMIN)
                throw new PlatformException(-1, "该团队人员已有权限");
            if (addTeamRelation.getType() == TeamConstants.TYPE_TEAM_OWNER)
                throw new PlatformException(-1, "团队拥有者不能给予自己权限");

            addTeamRelation.setType(TeamConstants.TYPE_TEAM_ADMIN);
            addTeamRelation.setUpdateTime(new Date());
            teamRelationRep.save(addTeamRelation);
        } else if (type == TeamConstants.TYPE_TEAM_NORMAL) {
            TeamRelation addTeamRelation = getTeamRelation(teamUserId, teamId);
            if (addTeamRelation.getType() == TeamConstants.TYPE_TEAM_NORMAL)
                throw new PlatformException(-1, "该团队人员已有权限");
            if (addTeamRelation.getType() == TeamConstants.TYPE_TEAM_OWNER)
                throw new PlatformException(-1, "团队拥有者不能给予自己权限");

            addTeamRelation.setType(TeamConstants.TYPE_TEAM_NORMAL);
            addTeamRelation.setUpdateTime(new Date());
            teamRelationRep.save(addTeamRelation);
        } else {
            throw new PlatformException(-1, "不能给予拥有者权限");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void giveUpTeamOwner(String userId, String teamId, String teamUserId) throws PlatformException {
        TeamRelation teamRelation = getTeamRelation(userId, teamId);
        if (teamRelation == null)
            throw new PlatformException(-1, "不属于该团队");
        if (teamRelation.getType() != TeamConstants.TYPE_TEAM_OWNER)
            throw new PlatformException(-1, "只有团队拥有者给予团队人员权限");

        TeamRelation addTeamRelation = getTeamRelation(teamUserId, teamId);
        if (addTeamRelation.getType() == TeamConstants.TYPE_TEAM_OWNER)
            throw new PlatformException(-1, "团队拥有者不能给予自己权限");

        addTeamRelation.setType(TeamConstants.TYPE_TEAM_OWNER);
        addTeamRelation.setUpdateTime(new Date());

        teamRelation.setType(TeamConstants.TYPE_TEAM_NORMAL);
        teamRelation.setUpdateTime(addTeamRelation.getUpdateTime());

        teamRelationRep.save(teamRelation);
        teamRelationRep.save(addTeamRelation);
    }

    @Transactional(rollbackFor = Exception.class)
    public void releaseTeam(String userId, String teamId) throws PlatformException {
        TeamRelation teamRelation = getTeamRelation(userId, teamId);
        if (teamRelation == null)
            throw new PlatformException(-1, "不属于该团队");
        if (teamRelation.getType() != TeamConstants.TYPE_TEAM_OWNER)
            throw new PlatformException(-1, "只有团队拥有者才能解散团队");

        teamRelationRep.deleteAllByTeamId(teamId);
        teamRep.delete(teamId);
    }

    /**
     * 检查用户是否有权限查看团队成员的数据
     *
     * @param teamId
     * @param userId
     * @param teamUserId
     */
    public void checkPermission(String teamId, String userId, String teamUserId) throws PlatformException {
        if (DataCheckUtil.isEmpty(teamId))
            throw new PlatformException(-1, "必须是团队成员");
        TeamRelation teamRelationAdmin = getTeamRelation(userId, teamId);
        if (null == teamRelationAdmin)
            throw new PlatformException(-1, "必须是团队成员");
        if (teamRelationAdmin.getType() != TeamConstants.TYPE_TEAM_OWNER && teamRelationAdmin.getType() != TeamConstants.TYPE_TEAM_ADMIN)
            throw new PlatformException(-1, "你没有查看权限");
        if (!DataCheckUtil.isEmpty(teamUserId)) {
            TeamRelation teamRelation = getTeamRelation(teamUserId, teamId);
            if (null == teamRelation)
                throw new PlatformException(-1, "该成员不是团队成员");
        }
    }

    public Team updateTeamInformation(String userId, Team team) throws PlatformException {
        TeamRelation teamRelation = teamRelationRep.findOne(new TeamRelation.TeamRelationId(team.getTeamId(), userId));
        if (teamRelation==null)
            throw new PlatformException(-1,"你不属于该团队");
        if (teamRelation.getType() != TeamConstants.TYPE_TEAM_OWNER && teamRelation.getType() != TeamConstants.TYPE_TEAM_ADMIN)
            throw new PlatformException(-1, "你没有修改权限");
        Team result = teamRep.findOne(team.getTeamId());
        if (!DataCheckUtil.isEmpty(team.getTeamBadge())) {
            result.setTeamBadge(team.getTeamBadge());
        } else if (!DataCheckUtil.isEmpty(team.getTeamName())) {
            result.setTeamName(team.getTeamName());
        } else if (!DataCheckUtil.isEmpty(team.getTeamDesc())) {
            result.setTeamDesc(team.getTeamDesc());
        } else if (!DataCheckUtil.isEmpty(team.getTeamIndustry())) {
            result.setTeamIndustry(team.getTeamIndustry());
        } else if (!DataCheckUtil.isEmpty(team.getTeamRegion())) {
            result.setTeamRegion(team.getTeamRegion());
        }
        result.setUpdateTime(new Date());
        return teamRep.save(result);
    }
}
