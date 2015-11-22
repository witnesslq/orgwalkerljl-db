package org.walkerljl.db.transaction;

import java.sql.Connection;

/**
 *
 * 事务隔离等级
 *
 * @author lijunlin
 */
public enum TransactionIsolationLevel {

	/**
	 * 不支持事务
	 */
	NONE(Connection.TRANSACTION_NONE),
	
	/**
	 * 不可以发生脏读；不可重复读和虚读可以发生。
	 * 只禁止事务读取其中带有未提交更改的行。
	 */
	READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
	
	/**
	 * 可以发生脏读、不可重复读和虚读
	 * 此级别允许被某一事务更改的行在已提交该行的所有更改之前被另一事务读取("脏读")。如果所有更改都被回滚，则第二个事务将获取无效的行。
	 */
	READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
	
	/**
	 * 指示不可以发生脏读和不可重复读的常量；虚读可以发生。此级别禁止事务读取带有未提交更改的行，它还禁止这种情况：一个事务读取某一行，而另一个事务更改该行，第一个事务重新读取该行，并在第二次读取时获得不同的值（“不可重复读”）。 
	 */
	REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
	
	/**
	 * 指示不可以发生脏读、不可重复读和虚读的常量。此级别包括 TRANSACTION_REPEATABLE_READ 中禁止的事项，同时还禁止出现这种情况：某一事务读取所有满足 WHERE 条件的行，
	 * 另一个事务插入一个满足 WHERE 条件的行，第一个事务重新读取满足相同条件的行，并在第二次读取时获得额外的“虚”行。 
	 */
	SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);
	
	private final int level;

	private TransactionIsolationLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}  
}