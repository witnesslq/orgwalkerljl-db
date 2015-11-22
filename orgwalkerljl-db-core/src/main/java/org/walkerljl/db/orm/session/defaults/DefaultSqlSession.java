package org.walkerljl.db.orm.session.defaults;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import org.walkerljl.commons.collection.CollectionUtils;
import org.walkerljl.db.orm.executor.Executor;
import org.walkerljl.db.orm.executor.defaults.DefaultExecutor;
import org.walkerljl.db.orm.session.Configuration;
import org.walkerljl.db.orm.session.SqlSession;
import org.walkerljl.db.orm.sql.SqlEntry;
import org.walkerljl.db.orm.sql.SqlGenerator;

/**
 *
 * DefaultSqlSession
 *
 * @author lijunlin
 */
public class DefaultSqlSession implements SqlSession {

	private Configuration configuration;
	private Executor executor;
	
	public DefaultSqlSession(Configuration configuration) {
		this.configuration = configuration;
		this.executor = new DefaultExecutor(this.configuration.getDataSource());
	}
	
	@Override
	public <T> int insert(T entity) {
		SqlEntry sqlEntry = SqlGenerator.generateInsertSql(entity);
		if (sqlEntry == null) {
			return 0;
		}
		executor.insertReturnPK(sqlEntry.getSql(), sqlEntry.getParams());
		return 1;
	}

	@Override
	public <T> int insert(List<T> entities) {
		if (CollectionUtils.isEmpty(entities)) {
			return 0;
		}
		int counter = 0;
		for (T entity : entities) {
			counter += insert(entity);
		}
		return counter;
	}

	@Override
	public <T> int delete(T entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> int update(T entity, T condition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> List<T> selectOne(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> selectList(T entity) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public <T> int selectCount(T entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Configuration getConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Connection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <KEY extends Serializable, T> int deleteByKey(KEY key, T entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <KEY extends Serializable, T> int deleteByKeys(List<KEY> keys,
			T entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <KEY extends Serializable, T> int updateByKey(KEY key, T entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <KEY extends Serializable, T> int updateByKeys(List<KEY> keys,
			T entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <KEY extends Serializable, T> T selectByKey(KEY key, T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <KEY extends Serializable, T> List<T> selectByKeys(List<KEY> keys,
			T entity) {
		// TODO Auto-generated method stub
		return null;
	}
}