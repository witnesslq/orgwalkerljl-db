package org.walkerljl.db.orm.sql;

import java.io.Serializable;

/**
 *
 * SqlEntry
 *
 * @author lijunlin
 */
public class SqlEntry implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String sql;
	private Object[] params;
	
	public SqlEntry() {}
	
	public SqlEntry(String sql, Object[] params) {
		super();
		this.sql = sql;
		this.params = params;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}
}