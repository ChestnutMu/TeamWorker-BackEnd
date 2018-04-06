package com.info.xiaotingtingBackEnd.repository;

import com.info.xiaotingtingBackEnd.model.DepartmentMemberRelation;
import com.info.xiaotingtingBackEnd.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/3 17:07:15
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Repository
public interface DepartmentMemberRep extends BaseRepository<DepartmentMemberRelation, DepartmentMemberRelation.DepartmentMemberRelationId> {
}
