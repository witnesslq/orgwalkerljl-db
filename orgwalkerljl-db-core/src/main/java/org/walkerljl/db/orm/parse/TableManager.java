/*
 * Copyright (c) 2010-2015 www.walkerljl.org All Rights Reserved.
 * The software source code all copyright belongs to the author, 
 * without permission shall not be any reproduction and transmission.
 */
package org.walkerljl.db.orm.parse;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.walkerljl.commons.cache.Cache;
import org.walkerljl.commons.cache.LRUCache;
import org.walkerljl.db.orm.entity.Table;
import org.walkerljl.log.Logger;
import org.walkerljl.log.LoggerFactory;

/**
 * TableManager 
 *
 * @author lijunlin
 */
public class TableManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TableManager.class);

	private ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock readLock = lock.readLock();
	private final Lock writeLock = lock.writeLock();

	private Cache<Class<?>, Table> tableCache = new LRUCache<Class<?>, Table>(100);
	
	private TableManager() {}
	
	public static TableManager getInstance() {
		return TableManagerHolder.instance;
	}
	
	public Table getTable(Class<?> entityClass) {
		readLock.lock();
		Table table = null;
		try {
			table = tableCache.get(entityClass);
			if (table == null) {
				readLock.unlock();
				writeLock.lock();
				try {
					if (table == null) {
						table = TableParser.parse(entityClass);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(String.format("Parse entity : %s to table", entityClass));
						}
					}
				} finally {
					writeLock.unlock();
					readLock.lock();
				}
			}
		} finally {
			readLock.unlock();
		}
		return table;
	}
	
	private static class TableManagerHolder {
		private static TableManager instance = new TableManager();
	}
}