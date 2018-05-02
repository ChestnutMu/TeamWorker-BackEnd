package com.info.xiaotingtingBackEnd.service.base;

import com.google.gson.Gson;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.repository.*;
import com.info.xiaotingtingBackEnd.repository.base.BaseRepository;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.socket.SenderEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/31 09:47:22
 * Description：Service基类
 * Email: xiaoting233zhang@126.com
 */
public abstract class BaseService<T, ID extends Serializable, TR extends BaseRepository<T, ID>> {

    public abstract TR getRepo();

    public Logger logger = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    public Gson gson = new Gson();

    @Autowired
    public UserRep userRep;
    @Autowired
    public DepartmentRep departmentRep;

    @Autowired
    public DepartmentRelationRep departmentRelationRep;

    @Autowired
    public DepartmentMemberRep departmentMemberRep;

    @Autowired
    public MessageRep messageRep;

    @Autowired
    public NotificationRep notificationRep;

    @Autowired
    public AttendanceRep attendanceRep;

    @Autowired
    public DayReportRep dayReportRep;

    @Autowired
    public WeekReportRep weekReportRep;

    @Autowired
    public MonthReportRep monthReportRep;

    @Autowired
    public PerformanceRep performanceRep;

    @Autowired
    public AddressRep addressRep;

    @Autowired
    public WorkOffRep workOffRep;

    @Autowired
    public UseGoodRep useGoodRep;

    @Autowired
    public PurchaseRep purchaseRep;

    @Autowired
    public ReimbursementRep reimbursementRep;

    @Autowired
    public UserRelationRep userRelationRep;

    @Autowired
    public NewFriendRequestRep newFriendRequestRep;

    @Autowired
    public PermissionRep permissionRep;

    @Autowired
    public UserPermissionRelationRep userPermissionRelationRep;

    @Autowired
    public TeamRep teamRep;

    @Autowired
    public TeamRelationRep teamRelationRep;

    @Autowired
    public ChatRep chatRep;

    @Autowired
    public ChatMessageRep chatMessageRep;

    @Autowired
    public SenderEventHandler handler;


    public List<T> listBySQL(String sql) {
        return getRepo().listBySQL(sql);
    }

    public T getUniqueBySearchCondition(SearchCondition searchCondition) {
        return getRepo().getUniqueBySearchCondition(searchCondition);
    }

    public List<T> getListBySearchCondition(SearchCondition searchCondition) {
        return getRepo().getListBySearchCondition(searchCondition);
    }

    public ApiResponse<List<T>> getPageBySearchCondition(SearchCondition searchCondition) {
        return getRepo().getPageBySearchCondition(searchCondition);
    }

    public long countBySearchCondition(SearchCondition searchCondition) {
        return getRepo().countBySearchCondition(searchCondition);
    }

    public EntityManager getEntityManager() {
        return getRepo().getEntityManager();
    }

    public <S extends T> S save(S var1) {
        return getRepo().save(var1);
    }

    public <S extends T> Iterable<S> save(Iterable<S> var1) {
        return getRepo().save(var1);
    }

    public T findOne(ID var1) {
        return getRepo().findOne(var1);
    }

    public boolean exists(ID var1) {
        return getRepo().exists(var1);
    }

    public Iterable<T> findAll() {
        return getRepo().findAll();
    }

    public Iterable<T> findAll(Iterable<ID> var1) {
        return getRepo().findAll(var1);
    }

    public long count() {
        return getRepo().count();
    }

    public void delete(ID var1) {
        getRepo().delete(var1);
    }

    public void delete(T var1) {
        getRepo().delete(var1);
    }

    public void delete(Iterable<? extends T> var1) {
        getRepo().delete(var1);
    }

    public void deleteAll() {
        getRepo().deleteAll();
    }

}
