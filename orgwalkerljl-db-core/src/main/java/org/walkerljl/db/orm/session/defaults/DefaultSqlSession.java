package org.walkerljl.db.orm.session.defaults;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.walkerljl.commons.util.Assert;
import org.walkerljl.db.orm.entity.SqlEntry;
import org.walkerljl.db.orm.executor.Executor;
import org.walkerljl.db.orm.executor.defaults.DefaultExecutor;
import org.walkerljl.db.orm.session.Configuration;
import org.walkerljl.db.orm.session.SqlSession;
import org.walkerljl.db.orm.sql.SqlGenerator;

/**
 *
 * DefaultSqlSession
 *
 * @author lijunlin
 */
public class DefaultSqlSession implements SqlSession {

	private static final String MESSAGE_SQL_IS_NULL = "生成SQL对象为NULL";
	private Configuration configuration;
	private Executor executor;
		
	/**
	 * 构造函数
	 * @param configuration
	 */
	public DefaultSqlSession(Configuration configuration) {
		this.configuration = configuration;
		this.executor = new DefaultExecutor(this.configuration.getDataSource());
	}
	
	public DefaultSqlSession(DataSource dataSource) {
		this.executor = new DefaultExecutor(dataSource);
	}

	@Override @SuppressWarnings("unchecked")
	public <T> int insert(T... entities) {
		SqlEntry sqlEntry = SqlGenerator.generateBatchInsertSql(entities);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return executor.update(sqlEntry.getSql(), sqlEntry.getParams());
	}

	@Override @SuppressWarnings("unchecked")
	public <KEY extends Serializable, T> int deleteByKeys(Class<T> entityClass, KEY... keys) {
		SqlEntry sqlEntry = SqlGenerator.generateDeleteByKeysSql(entityClass, keys);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return executor.update(sqlEntry.getSql(), sqlEntry.getParams());
	}

	@Override
	public <T> int delete(T entity) {
		SqlEntry sqlEntry = SqlGenerator.generateDeleteSql(entity);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return executor.update(sqlEntry.getSql(), sqlEntry.getParams());
	}

	@Override @SuppressWarnings("unchecked")
	public <KEY extends Serializable, T> int updateByKeys(T entity, KEY... keys) {
		SqlEntry sqlEntry = SqlGenerator.generateUpdateByKeysSql(entity, keys);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return executor.update(sqlEntry.getSql(), sqlEntry.getParams());
	}

	@Override
	public <T> int update(T entity, T condition) {
		SqlEntry sqlEntry = SqlGenerator.generateUpdateSql(entity, condition);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return executor.update(sqlEntry.getSql(), sqlEntry.getParams());
	}

	@Override @SuppressWarnings("unchecked")
	public <KEY extends Serializable, T> List<T> selectByKeys(Class<T> entityClass, KEY... keys) {
		SqlEntry sqlEntry = SqlGenerator.generateSelectByKeysSql(entityClass, keys);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return (List<T>) executor.queryEntityList(entityClass, sqlEntry.getSql(), sqlEntry.getParams());
	}
	
	@Override @SuppressWarnings("unchecked")
	public <T> T selectOne(T entity) {
		SqlEntry sqlEntry = SqlGenerator.generateSelectSql(entity, 1, 1);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return (T) executor.queryEntity(entity.getClass(), sqlEntry.getSql(), sqlEntry.getParams());
	}
	
	@Override @SuppressWarnings("unchecked")
	public <T> List<T> selectList(T entity, int currentPage, int pageSize) {
		SqlEntry sqlEntry = SqlGenerator.generateSelectSql(entity, currentPage, pageSize);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return (List<T>) executor.queryEntityList(entity.getClass(), sqlEntry.getSql(), sqlEntry.getParams());
	}
	
	@Override
	public <T> int selectCount(T entity) {
		SqlEntry sqlEntry = SqlGenerator.generateSelectCountSql(entity);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return (int) executor.queryEntity(entity.getClass(), sqlEntry.getSql(), sqlEntry.getParams());
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
		return configuration;
	}

	@Override
	public Connection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}	
}