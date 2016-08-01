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
	MYSQL(1, "MySQL"),

	/**
	 * ORACLE
	 */
	ORACLE(2, "ORACLE"),

	/**
	 * SQLSERVER
	 */
	SQLSERVER(3, "SQLSERVER");

	/**
	 * 类型值
	 */
	private final Integer value;
	/**
	 * 类型名称
	 */
	private final String name;

	/**
	 * 私有构造函数
	 *
	 * @param value 类型值
	 * @param name  类型名称
	 */
	private DatabaseType(Integer value, String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * 获取类型值
	 *
	 * @return
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * 获取类型名称
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取类型对象
	 *
	 * @param value
	 * @return
	 */
	public static DatabaseType getType(Integer value) {
		if (value == null || value.intValue() == 0) {
			return null;
		}
		for (DatabaseType element : DatabaseType.values()) {
			if (element.getValue().intValue() == value.intValue()) {
				return element;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return getValue().toString();
	}
}