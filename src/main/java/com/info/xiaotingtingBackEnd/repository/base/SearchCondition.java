package com.info.xiaotingtingBackEnd.repository.base;

import java.util.ArrayList;
import java.util.List;
/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/11 16:21:47
 * Description：数据库查询条件
 * Email: xiaoting233zhang@126.com
 */
public class SearchCondition {
    private static final Integer DEFUALT_PAGE_NUM = 1;
    private static final Integer DEFUALT_SIZE = 10;
    public static final String JOIN_TYPE_AND="and";
    public static final String JOIN_TYPE_OR="or";
    private  String joinType;
    private Integer pageNum;
    private Integer size;
    private List<SearchBean> searchBeans;
    private List<SearchBean> sortBeans;

    public SearchCondition() {
        this(DEFUALT_PAGE_NUM, DEFUALT_SIZE);
    }

    public SearchCondition(Integer pageNum, Integer size) {
        if(pageNum <= 0)
            pageNum = 1;
        if(size <= 0)
            size = 10;
        if(size > 20)
            size = 20;
        this.pageNum = pageNum;
        this.size = size;
        this.joinType =JOIN_TYPE_AND;
        searchBeans = new ArrayList<>(5);
    }

    /**
     * 添加排序条件
     *
     * @param key      字段名
     * @param value    值
     * @param operator 操作 如果为sort，则值可取desc或asc，表示降序或升序
     */
    public void addSortBean(String key, Object value, String operator) {
        SearchBean searchBean = new SearchBean();
        searchBean.setKey(key);
        searchBean.setOperator(operator);
        searchBean.setPriority(0);
        searchBean.setValue(value);
        if (sortBeans == null)
            sortBeans = new ArrayList<>();
        sortBeans.add(searchBean);
    }

    /**
     * 添加搜索条件
     *
     * @param key      字段名
     * @param value    值
     * @param operator 操作 如果为sort，则值可取desc或asc，表示降序或升序
     * @param priority 排序优先级，如果不排序默认设为-1，0的优先级最高
     */
    public void addSearchBean(String key, Object value, String operator, Integer priority) {
        SearchBean searchBean = new SearchBean();
        searchBean.setKey(key);
        searchBean.setOperator(operator);
        searchBean.setPriority(priority);
        searchBean.setValue(value);
        if (searchBean.getPriority() != 0)
            this.searchBeans.add(searchBean);
        if (searchBean.getPriority() <= -1)
            this.searchBeans.add(searchBean);
        else {
            if (sortBeans == null)
                sortBeans = new ArrayList<>();
            sortBeans.add(searchBean);
        }
    }

    /**
     * 添加无排序条件的搜索条件
     *
     * @param key
     * @param value
     * @param operator
     */
    public void addSearchBean(String key, Object value, String operator) {
        SearchBean searchBean = new SearchBean();
        searchBean.setKey(key);
        searchBean.setOperator(operator);
        searchBean.setValue(value);
        searchBean.setPriority(0);
        this.searchBeans.add(searchBean);
    }

    /**
     * 直接添加条件列表
     *
     * @param searchBeanList
     * @return
     */
    public void addSearchBeanList(List<SearchBean> searchBeanList) {
        this.sortBeans.addAll(searchBeanList);
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        if (pageNum <= 0)
            this.pageNum = 1;
        else
            this.pageNum = pageNum;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        if (size <= 0)
            this.size = 1;
        else
            this.size = size;
    }

    public List<SearchBean> getSearchBeans() {
        return searchBeans;
    }

    public void setSearchBeans(List<SearchBean> searchBeans) {
        this.searchBeans = searchBeans;
    }

    public List<SearchBean> getSortBeans() {
        return sortBeans;
    }

    public void setSortBeans(List<SearchBean> sortBeans) {
        this.sortBeans = sortBeans;
    }

    public String getJoinType() {
        return joinType;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }
}
