/*
 * Copyright (c) 2010-2015 www.walkerljl.org All Rights Reserved.
 * The software source code all copyright belongs to the author, 
 * without permission shall not be any reproduction and transmission.
 */
package org.walkerljl.db.orm.sql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * EntityUpdateCondition 
 *
 * @author lijunlin<walkerljl@qq.com>
 */
public class EntityUpdateCondition implements Serializable {

	private static final long serialVersionUID = 1L;

	private StringBuilder condition = new StringBuilder();
	private List<Object> parameterValues = new ArrayList<Object>();
	private int parameterIndex = -1;
	private boolean ignore = false;
	
	public EntityUpdateCondition create() {
		return new EntityUpdateCondition();
	}
	
	public EntityUpdateCondition key() {
		condition.append(" set");
		ignore = true;
		return this;
	}
	
	public EntityUpdateCondition condition(String key, Object value) {
		if (parameterIndex >= 0) {
			condition.append(",");
		}
		condition.append(key).append("=?");
		parameterValues.add(value);
		parameterIndex ++;
		return this;
	}
	
	public List<Object> getParameterValues() {
		return parameterValues;
	}

	public boolean isIgnore() {
		return ignore;
	}

	@Override
	public String toString() {
		return condition.toString();
	}
}