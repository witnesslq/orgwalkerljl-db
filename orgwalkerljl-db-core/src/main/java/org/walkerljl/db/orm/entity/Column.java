/*
 * Copyright (c) 2010-2015 www.walkerljl.org All Rights Reserved.
 * The software source code all copyright belongs to the author, 
 * without permission shall not be any reproduction and transmission.
 */
package org.walkerljl.db.orm.entity;

import java.io.Serializable;

/**
 * 字段 
 *
 * @author lijunlin
 */
public class Column implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 名称*/
	private String name;
	/** 别名*/
	private String alias;
	/** java 类型*/
	private Class<?> javaType;
	/** 是否主键*/
	private boolean isPrimaryKey;
	/** 是否自增长*/
	private boolean autoIncrement;
	/** 长度*/
	private int length;
	/** 是否为空*/
	private boolean nullAble;
	/** 默认值*/
	private Object defaultValue;
	/** 备注*/
	private String comment;
	
	/** 映射的实体属性名称*/
	private String fieldName;
	
	/**
	 * 默认构造函数
	 */
	public Column() {}

	//============自定义方法
	/**
	 * 获取Getter名称
	 * @return
	 */
	public String getGetterName() {
		return "get" + (fieldName.charAt(0) + "").toUpperCase() + fieldName.substring(1);
	}
	
	/**
	 * 是否数值类型
	 * @return
	 */
	public boolean isNumeric() {
		return javaType == Long.class || javaType == Integer.class || javaType == Short.class || javaType == Float.class || javaType == Double.class;
	}
	
	//============getter and setter
	/**
	 * 获取名称
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取别名
	 * @return
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * 设置别名
	 * @param alias
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * 获取类型
	 * @return
	 */
	public Class<?> getJavaType() {
		return javaType;
	}

	/**
	 * 设置类型
	 * @param javaType
	 */
	public void setJavaType(Class<?> javaType) {
		this.javaType = javaType;
	}

	/**
	 * 是否为主键
	 * @return
	 */
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	/**
	 * 设置是否为主键
	 * @param isPrimaryKey
	 */
	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	/**
	 * 是否为自动增长
	 * @return
	 */
	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	/**
	 * 设置是否为自动增长
	 * @param autoIncrement
	 */
	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	/**
	 * 获取长度
	 * @return
	 */
	public int getLength() {
		return length;
	}

	/**
	 * 设置长度
	 * @param length
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * 是否允许为空
	 * @return
	 */
	public boolean isNullAble() {
		return nullAble;
	}

	/**
	 * 设置是否允许为空
	 * @param nullAble
	 */
	public void setNullAble(boolean nullAble) {
		this.nullAble = nullAble;
	}

	/**
	 * 获取默认值
	 * @return
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}

	/**
	 * 设置默认值
	 * @param defaultValue
	 */
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
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

	/**
	 * 获取属性名称
	 * @return
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * 设置属性名称
	 * @param fieldName
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}