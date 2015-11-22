/*
 * Copyright (c) 2010-2015 www.walkerljl.org All Rights Reserved.
 * The software source code all copyright belongs to the author, 
 * without permission shall not be any reproduction and transmission.
 */
package org.walkerljl.db.orm.entity;

import java.io.Serializable;

import org.walkerljl.db.orm.enums.DatabaseType;

/**
 * Database 
 *
 * @author lijunlin<walkerljl@qq.com>
 */
public class Database implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 数据库类型*/
	private DatabaseType type;
	/** 名称*/
	private String name;
	/** 字符集*/
	private String charset;
	/** 引擎*/
	private String engine;
	
	public Database() {}

	public DatabaseType getType() {
		return type;
	}

	public void setType(DatabaseType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}
}