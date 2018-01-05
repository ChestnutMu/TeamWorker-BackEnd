package com.info.xiaotingtingBackEnd.repository.base;

import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/11 16:21:47
 * Description：所有repositiory都继承这个接口
 * Email: xiaoting233zhang@126.com
 */

@NoRepositoryBean
public interface BaseRepository<T,ID extends Serializable> extends JpaRepository<T,ID>,JpaSpecificationExecutor<T> {

    List<T> listBySQL(String sql);

    T getUniqueBySearchCondition(SearchCondition searchCondition);

    List<T> getListBySearchCondition(SearchCondition searchCondition);

    ApiResponse<List<T>> getPageBySearchCondition(SearchCondition searchCondition);

    long countBySearchCondition(SearchCondition searchCondition);

    EntityManager getEntityManager();
}
