package com.info.xiaotingtingBackEnd.repository.base;

import java.util.List;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/4/10 08:21:47
 * Description：数据库查询条件
 * Email: xiaoting233zhang@126.com
 */
public class SearchRequest {
    private Integer pageNum;
    private Integer pageSize;
    private List<SearchBean> searchBeans;
    private List<SearchBean> sortBeans;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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
}
