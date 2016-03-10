package org.walkerljl.db.transaction;

import org.walkerljl.commons.exception.UncheckedException;

/**
 * 事务异常
 * 
 * @author lijunlin
 */
public class TransactionException extends UncheckedException {

	private static final long serialVersionUID = -6786549876849535944L;

	/**
	 * 默认构造函数
	 */
	public TransactionException() {
		super();
	}
	
	/**
	 * 构造函数
	 * @param message
	 */
	public TransactionException(String message) {
		super(message);
	}
	
	/**
	 * 构造函数
	 * @param e
	 */
	public TransactionException(Throwable e) {
		super(e);
	}
	
	/**
	 * 构造函数
	 * @param msg
	 * @param e
	 */
	public TransactionException(String msg, Throwable e) {
		super(msg,e);
	}
}