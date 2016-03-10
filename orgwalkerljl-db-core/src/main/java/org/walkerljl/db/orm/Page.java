/*
 * Copyright (c) 2010-present www.walkerljl.org All Rights Reserved.
 * The software source code all copyright belongs to the author, 
 * without permission shall not be any reproduction and transmission.
 */
package org.walkerljl.db.orm;

import java.io.Serializable;
import java.util.List;

/**
 * Page
 * 
 * @author lijunlin
 */
public class Page<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
		
	private static final int DEFAULT_MAX_PAGE_SIZE = 500;
	private static final int DEFAULT_MIN_PAGE_SIZE = 20;
	
	private int pageSize = DEFAULT_MIN_PAGE_SIZE;
    private int totalCount;
    private int currentPage;
	private List<T> data;

	public Page() {}

	public Page(int currentPage) {
        this.currentPage = currentPage;
    }    

	public Page(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }
	
	 /**
     * 获取开始索引
     * @return
     */
    public int getStartIndex() {
        return (getCurrentPage() - 1) * this.pageSize;
    }

    /**
     * 获取结束索引
     * @return
     */
    public int getEndIndex() {
        return getCurrentPage() * this.pageSize;
    }

    /**
     * 是否第一页
     * @return
     */
    public boolean isFirstPage() {
        return getCurrentPage() <= 1;
    }

    /**
     * 是否末页
     * @return
     */
    public boolean isLastPage() {
        return getCurrentPage() >= getPageCount();
    }

    /**
     * 获取下一页页码
     * @return
     */
    public int getNextPage() {
        return isLastPage() ? getCurrentPage() : getCurrentPage() + 1;
    }

    /**
     * 获取上一页页码
     * @return
     */
    public int getPreviousPage() {
        return isFirstPage() ? 1 : getCurrentPage() - 1;
    }

    /**
     * 获取当前页页码
     * @return
     */
    public int getCurrentPage() {
        return currentPage  <= 0 ? 1 : currentPage;
    }

    /**
     * 取得总页数
     * @return
     */
    public int getPageCount() {
        return (totalCount % pageSize == 0) ? (totalCount / pageSize) : (totalCount / pageSize + 1);
    }

    /**
     * 取总记录数.
     * @return
     */
    public int getTotalCount() {
        return this.totalCount;
    }

    /**
     * 设置当前页
     * @param currentPage
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * 获取每页数据容量.
     * @return
     */
    public int getPageSize() {
        return pageSize > DEFAULT_MAX_PAGE_SIZE ? DEFAULT_MAX_PAGE_SIZE : pageSize;
    }
    
    public void setPageSize(int pageSize) {
        if(pageSize > DEFAULT_MAX_PAGE_SIZE) {
            pageSize = DEFAULT_MAX_PAGE_SIZE;//最大DEFAULT_MAX_PAGE_SIZE条
        }
        this.pageSize = pageSize;
    }
    
    /**
     * 该页是否有下一页.
     * @return
     */
    public boolean hasNextPage() {
        return getCurrentPage() < getPageCount();
    }

    /**
     * 该页是否有上一页.
     * @return
     */
    public boolean hasPreviousPage() {
        return getCurrentPage() > 1;
    }

    /**
     * 获取数据集
     * @return
     */
	public List<T> getResult() {
        return data;
    }

    /**
     * 设置数据集
     * @param data
     */
	public void setResult(List<T> data) {
        this.data = data;
    }

    /**
     * 设置总记录条数
     * @param totalCount
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}