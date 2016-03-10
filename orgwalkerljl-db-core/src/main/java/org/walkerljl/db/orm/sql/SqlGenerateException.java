package org.walkerljl.db.orm.sql;

import org.walkerljl.commons.exception.UncheckedException;

/**
 * SQL生成异常
 * 
 * @author lijunlin
 */
public class SqlGenerateException extends UncheckedException {

	private static final long serialVersionUID = -6786549876849535944L;

	/**
	 * 默认构造函数
	 */
	public SqlGenerateException() {
		super();
	}
	
	/**
	 * 构造函数
	 * @param message
	 */
	public SqlGenerateException(String message) {
		super(message);
	}
	
	/**
	 * 构造函数
	 * @param e
	 */
	public SqlGenerateException(Throwable e) {
		super(e);
	}
	
	/**
	 * 构造函数
	 * @param msg
	 * @param e
	 */
	public SqlGenerateException(String msg, Throwable e) {
		super(msg,e);
	}
}