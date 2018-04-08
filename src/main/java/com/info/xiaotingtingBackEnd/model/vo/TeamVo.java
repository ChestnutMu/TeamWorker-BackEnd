package com.info.xiaotingtingBackEnd.model.vo;

import com.info.xiaotingtingBackEnd.model.Team;

import java.util.List;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/7 21:07:19
 * Description：
 * Email: xiaoting233zhang@126.com
 */
public class TeamVo {

    private Team team;

    private List<String> userIds;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
