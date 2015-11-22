package org.walkerljl.db.orm.session;

import javax.sql.DataSource;

/**
 *
 * Configuration
 *
 * @author lijunlin
 */
public class Configuration {

	private DataSource dataSource;
	
	public Configuration() {}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
