package org.walkerljl.db.orm.enums;
/**
 *
 * DatabaseType
 *
 * @author lijunlin
 */
public enum DatabaseType {

	/**
	 * MySQL
	 */
	MYSQL("mysql", "MySQL");
	
	private String value;
	private String name;
	
	private DatabaseType(String value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
}