package com.info.xiaotingtingBackEnd.repository;

import com.info.xiaotingtingBackEnd.model.TeamRelation;
import com.info.xiaotingtingBackEnd.model.vo.TeamUserVo;
import com.info.xiaotingtingBackEnd.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by king on 19:55 2018/4/9
 */
@Repository
public interface TeamRelationRep extends BaseRepository<TeamRelation, TeamRelation.TeamRelationId> {

    @Query(value = "select new com.info.xiaotingtingBackEnd.model.vo.TeamUserVo(u.userId, u.nickname, u.avatar,u.telephone" +
            ", u.sex, u.birthday, u.region, tr.type, tr.updateTime) from TeamRelation tr,User u " +
            "where tr.teamId = :teamId and tr.userId = u.userId",
            countQuery = "select count(tr.userId) from TeamRelation tr " +
                    "where tr.teamId = :teamId ")
    List<TeamUserVo> getTeamers(@Param("teamId") String teamId);

    void deleteAllByTeamId(String teamId);

    Long countAllByTeamIdAndUserId(String teamId, String userId);
}
