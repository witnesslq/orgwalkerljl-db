package org.walkerljl.db.orm.session.defaults;

import org.walkerljl.commons.util.Assert;
import org.walkerljl.commons.util.ListUtils;
import org.walkerljl.db.orm.entity.SqlEntry;
import org.walkerljl.db.orm.executor.Executor;
import org.walkerljl.db.orm.executor.defaults.DefaultExecutor;
import org.walkerljl.db.orm.session.Configuration;
import org.walkerljl.db.orm.session.SqlSession;
import org.walkerljl.db.orm.sql.SqlGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

/**
 * 
 * DefaultSqlSession
 *
 * @author lijunlin
 *
 * @param <T>
 * @param <KEY>
 */
public class DefaultSqlSession<T, KEY extends Serializable> implements SqlSession<T, KEY>{

	private static final String MESSAGE_SQL_IS_NULL = "生成SQL对象为NULL";
	private Configuration configuration;
	private Executor executor;
	private SqlGenerator sqlGenerator;
		
	/**
	 * 构造函数
	 * @param configuration
	 */
	public DefaultSqlSession(Configuration configuration) {
		Assert.isTrue(configuration != null, "configuration 为NULL");
		Assert.isTrue(configuration.getDataSource() != null, "dataSource 为NULL");
		Assert.isTrue(configuration.getDatabaseType() != null, "databaseType 为NULL");
		this.configuration = configuration;
		this.executor = new DefaultExecutor(this.configuration.getDataSource());
		this.sqlGenerator = new SqlGenerator(this.configuration.getDatabaseType());
	}

	@Override @SuppressWarnings("unchecked")
	public KEY insertReturnPK(T entity) {
		SqlEntry sqlEntry = sqlGenerator.generateBatchInsertSql(ListUtils.newArrayList(entity));
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return (KEY) executor.insertReturnPK(sqlEntry.getSql(), sqlEntry.getParams());
	}

	@Override
	public int insert(List<T> entities) {
		SqlEntry sqlEntry = sqlGenerator.generateBatchInsertSql(entities);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return executor.update(sqlEntry.getSql(), sqlEntry.getParams());
	}

	@Override
	public int deleteByKey(Class<T> entityClass, KEY key) {
		return deleteByKeys(entityClass, ListUtils.newArrayList(key));
	}

	@Override
	public int deleteByKeys(Class<T> entityClass, List<KEY> keys) {
		SqlEntry sqlEntry = sqlGenerator.generateDeleteByKeysSql(entityClass, keys);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return executor.update(sqlEntry.getSql(), sqlEntry.getParams());
	}

	@Override
	public int delete(T condition) {
		SqlEntry sqlEntry = sqlGenerator.generateDeleteSql(condition);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return executor.update(sqlEntry.getSql(), sqlEntry.getParams());
	}

	@Override
	public int updateByKey(T entity, KEY key) {
		return updateByKeys(entity, ListUtils.newArrayList(key));
	}

	@Override
	public int updateByKeys(T entity, List<KEY> keys) {
		SqlEntry sqlEntry = sqlGenerator.generateUpdateByKeysSql(entity, keys);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return executor.update(sqlEntry.getSql(), sqlEntry.getParams());
	}

	@Override
	public int update(T entity, T condition) {
		SqlEntry sqlEntry = sqlGenerator.generateUpdateSql(entity, condition);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return executor.update(sqlEntry.getSql(), sqlEntry.getParams());
	}

	@Override
	public T selectByKey(Class<T> entityClass, KEY key) {
		SqlEntry sqlEntry = sqlGenerator.generateSelectByKeysSql(entityClass, ListUtils.newArrayList(key));
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return executor.queryEntity(entityClass, sqlEntry.getSql(), sqlEntry.getParams());
	}

	@Override
	public List<T> selectListByKeys(Class<T> entityClass, List<KEY> keys) {
		SqlEntry sqlEntry = sqlGenerator.generateSelectByKeysSql(entityClass, keys);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return executor.queryEntityList(entityClass, sqlEntry.getSql(), sqlEntry.getParams());
	}

	@Override @SuppressWarnings("unchecked")
	public List<T> selectList(T condition, int currentPage, int pageSize) {
		SqlEntry sqlEntry = sqlGenerator.generateSelectSql(condition, currentPage, pageSize);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return (List<T>) executor.queryEntityList(condition.getClass(), sqlEntry.getSql(), sqlEntry.getParams());
	}

	@Override
	public int selectListCount(T condition) {
		SqlEntry sqlEntry = sqlGenerator.generateSelectCountSql(condition);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return executor.queryCount(sqlEntry.getSql(), sqlEntry.getParams());
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
	public void close() {
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

	
}