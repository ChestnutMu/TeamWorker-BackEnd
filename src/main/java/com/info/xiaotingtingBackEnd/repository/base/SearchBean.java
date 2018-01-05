package com.info.xiaotingtingBackEnd.repository.base;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/11 16:21:47
 * Description：数据库查找
 * Email: xiaoting233zhang@126.com
 */
public class SearchBean {
    private String key;
    private Object value;
    private String operator;
    private Integer priority;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    public final static String OPERATOR_IN = "in";
    public final static String OPERATOR_LIKE = "like";
    public final static String OPERATOR_SORT = "sort";
    public final static String OPERATOR_IS_NULL = "is_null";
    public final static String OPERATOR_EQ = "=";
    public final static String OPERATOR_NE = "!=";
    public final static String OPERATOR_LT = "<";
    public final static String OPERATOR_GT = ">";
    public final static String OPERATOR_LE = "<=";
    public final static String OPERATOR_GE = ">=";

}
