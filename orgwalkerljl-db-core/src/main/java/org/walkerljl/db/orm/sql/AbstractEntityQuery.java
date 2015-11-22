/*
 * Copyright (c) 2010-2015 www.walkerljl.org All Rights Reserved.
 * The software source code all copyright belongs to the author, 
 * without permission shall not be any reproduction and transmission.
 */
package org.walkerljl.db.orm.sql;

/**
 * AbstractEntityQuery 
 *
 * @author lijunlin<walkerljl@qq.com>
 */
public abstract class AbstractEntityQuery {

	private StringBuilder sql = new StringBuilder();
	
	public AbstractEntityQuery alias(String aliasName) {
		sql.append(" ").append(aliasName);
		return this;
	}
	
	public AbstractEntityQuery where() {
		sql.append(" where");
		return this;
	}
	
	public AbstractEntityQuery and(String propertyName, Object value) {
		return this;
	}
	
	public AbstractEntityQuery or(String propertyName, Object value) {
		return this;
	}
}