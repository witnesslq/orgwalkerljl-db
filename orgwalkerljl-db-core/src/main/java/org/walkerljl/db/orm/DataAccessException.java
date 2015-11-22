package org.walkerljl.db.orm;

import org.walkerljl.commons.exception.UncheckedException;

/**
 * 数据访问异常
 * 
 * @author lijunlin
 */
public class DataAccessException extends UncheckedException {

	private static final long serialVersionUID = -6786549876849535944L;

	/**
	 * 默认构造函数
	 */
	public DataAccessException() {
		super();
	}
	
	/**
	 * 构造函数
	 * @param message
	 */
	public DataAccessException(String message) {
		super(message);
	}
	
	/**
	 * 构造函数
	 * @param e
	 */
	public DataAccessException(Throwable e) {
		super(e);
	}
	
	/**
	 * 构造函数
	 * @param msg
	 * @param e
	 */
	public DataAccessException(String msg, Throwable e) {
		super(msg,e);
	}
}