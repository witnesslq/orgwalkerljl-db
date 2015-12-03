/*
 * Copyright (c) 2010-2015 www.walkerljl.org All Rights Reserved.
 * The software source code all copyright belongs to the author, 
 * without permission shall not be any reproduction and transmission.
 */
package org.walkerljl.db.orm.entity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.walkerljl.commons.collection.CollectionUtils;
import org.walkerljl.commons.util.StringPool;

/**
 * 表
 *
 * @author lijunlin
 */
public class Table {

	/** 表名*/
	private String name;
	/** 主键*/
	private Column primaryKey;
	/** 字段列表*/
	private List<Column> columns;
	/** 备注*/
	private String comment;
	
	//扩展字段
	/** 字段、属性映射Map*/
	private Map<String, String> columnToPropertyMap = null;
	private String columnNameListString = "";
	
	/**
	 * 默认构造函数
	 */
	public Table() {}
	
	//============自定义方法
	/**
	 * 获取字段、属性映射Map
	 * @return
	 */
	public Map<String, String> getColumnToPropertyMap() {
		if (columnToPropertyMap == null) {
			columnToPropertyMap = new LinkedHashMap<String, String>();
			for (Column column : columns) {
				columnToPropertyMap.put(column.getName(), column.getFieldName());
			}
		}
		return columnToPropertyMap;
	}
	
	public String getColumnNameListString() {
		if ("".equals(columnNameListString)) {
			Map<String, String> columnToPropertyMap = getColumnToPropertyMap();
			if (columnToPropertyMap == null) {
				return StringPool.EMPTY;
			}
			return CollectionUtils.wrap(columnToPropertyMap.keySet(), ",");
		} else {
			return columnNameListString;
		}
	}

	//============getter and setter
	/**
	 * 获取表名
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置表名
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取主键
	 * @return
	 */
	public Column getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * 设置主键
	 * @param primaryKey
	 */
	public void setPrimaryKey(Column primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * 获取字段
	 * @return
	 */
	public List<Column> getColumns() {
		return columns;
	}

	/**
	 * 设置字段
	 * @param columns
	 */
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	/**
	 * 获取备注
	 * @return
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * 设置备注
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
}