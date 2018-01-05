package com.info.xiaotingtingBackEnd.pojo;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/11 15:49:46
 * Description：接口返回数据
 * Email: xiaoting233zhang@126.com
 */
public class ApiResponse<T> {
    private int status; 		// 返回码，0为成功，其他为失败
    private String message; 	// 返回信息
    private T data; 			// 返回数据
    private int currentPage; 	// 当前页数
    private int pageSize; 		// 每页显示数量
    private int maxCount; 		// 总条数
    private int maxPage; 		// 总页数

    public ApiResponse(){

    }
    // 构造函数，初始化status和message
    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // 判断结果是否成功
    public boolean isSuccess() {
        return status==0;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * @return the currentPage
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @param currentPage
     *            the currentPage to set
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize
     *            the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the maxCount
     */
    public int getMaxCount() {
        return maxCount;
    }

    /**
     * @param maxCount
     *            the maxCount to set
     */
    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    /**
     * @return the maxPage
     */
    public int getMaxPage() {
        return maxPage;
    }

    /**
     * @param maxPage
     *            the maxPage to set
     */
    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }
}
