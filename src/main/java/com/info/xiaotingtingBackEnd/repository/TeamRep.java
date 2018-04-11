package com.info.xiaotingtingBackEnd.repository;

import com.info.xiaotingtingBackEnd.model.Department;
import com.info.xiaotingtingBackEnd.model.Team;
import com.info.xiaotingtingBackEnd.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/8 12:18:09
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Repository
public interface TeamRep extends BaseRepository<Team, String> {

    Team findByTeamName(String name);

    @Query(value = "select t from TeamRelation tr,Team t " +
            "where tr.userId = :userId and tr.teamId = t.teamId",
            countQuery = "select count(t) from TeamRelation tr,Team t " +
                    "where tr.userId = :userId and tr.teamId = t.teamId")
    List<Team> getTeamByUserId(@Param("userId") String userId);
}
