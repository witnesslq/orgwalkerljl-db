package org.walkerljl.db.orm.session.defaults;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import org.walkerljl.commons.collection.ArraysUtils;
import org.walkerljl.commons.collection.CollectionUtils;
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
	private static final String MESSAGE_ENTITY_IS_NULL = "实体对象为NULL";
	private static final String MESSAGE_KEYS_IS_EMPTY = "主键列表为空";
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
	
	@Override
	public <T> int insert(T entity) {
		Assert.isTrue(entity != null, MESSAGE_ENTITY_IS_NULL);
		SqlEntry sqlEntry = SqlGenerator.generateInsertSql(entity);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		executor.insertReturnPK(sqlEntry.getSql(), sqlEntry.getParams());
		return 1;
	}

	@Override
	public <T> int insert(List<T> entities) {
		Assert.isTrue(CollectionUtils.isNotEmpty(entities), MESSAGE_ENTITY_IS_NULL);
		SqlEntry sqlEntry = SqlGenerator.generateInsertSql(entities);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return executor.update(sqlEntry.getSql(), sqlEntry.getParams());
	}

	@Override
	public <KEY extends Serializable, T> int deleteByKeys(T entity, KEY... keys) {
		Assert.isTrue(ArraysUtils.isNotEmpty(keys), MESSAGE_KEYS_IS_EMPTY);
		Assert.isTrue(entity != null, MESSAGE_ENTITY_IS_NULL);
		SqlEntry sqlEntry = SqlGenerator.generateDeleteByKeysSql(entity, keys);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return executor.update(sqlEntry.getSql(), sqlEntry.getParams());
	}

	@Override
	public <T> int delete(T entity) {
		Assert.isTrue(entity != null, MESSAGE_ENTITY_IS_NULL);
		SqlEntry sqlEntry = SqlGenerator.generateDeleteSql(entity);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return executor.update(sqlEntry.getSql(), sqlEntry.getParams());
	}

	@Override
	public <KEY extends Serializable, T> int updateByKeys(T entity, KEY... keys) {
		Assert.isTrue(ArraysUtils.isNotEmpty(keys), MESSAGE_KEYS_IS_EMPTY);
		Assert.isTrue(entity != null, MESSAGE_ENTITY_IS_NULL);
		SqlEntry sqlEntry = SqlGenerator.generateUpdateByKeysSql(entity, keys);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return executor.update(sqlEntry.getSql(), sqlEntry.getParams());
	}

	@Override
	public <T> int update(T entity, T condition) {
		Assert.isTrue(entity != null, MESSAGE_ENTITY_IS_NULL);
		SqlEntry sqlEntry = SqlGenerator.generateUpdateSql(entity, condition);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return executor.update(sqlEntry.getSql(), sqlEntry.getParams());
	}

	@Override @SuppressWarnings("unchecked")
	public <KEY extends Serializable, T> List<T> selectByKeys(T entity, KEY... keys) {
		Assert.isTrue(ArraysUtils.isNotEmpty(keys), MESSAGE_KEYS_IS_EMPTY);
		Assert.isTrue(entity != null, MESSAGE_ENTITY_IS_NULL);
		SqlEntry sqlEntry = SqlGenerator.generateSelectByKeysSql(entity, keys);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return (List<T>) executor.queryEntityList(entity.getClass(), sqlEntry.getSql(), sqlEntry.getParams());
	}
	
	@Override @SuppressWarnings("unchecked")
	public <T> T selectOne(T entity) {
		Assert.isTrue(entity != null, MESSAGE_ENTITY_IS_NULL);
		SqlEntry sqlEntry = SqlGenerator.generateSelectSql(entity, 1, 1);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return (T) executor.queryEntity(entity.getClass(), sqlEntry.getSql(), sqlEntry.getParams());
	}
	
	@Override @SuppressWarnings("unchecked")
	public <T> List<T> selectList(T entity, int currentPage, int pageSize) {
		Assert.isTrue(entity != null, MESSAGE_ENTITY_IS_NULL);
		SqlEntry sqlEntry = SqlGenerator.generateSelectSql(entity, currentPage, pageSize);
		Assert.isTrue(sqlEntry != null, MESSAGE_SQL_IS_NULL);
		return (List<T>) executor.queryEntityList(entity.getClass(), sqlEntry.getSql(), sqlEntry.getParams());
	}
	
	@Override
	public <T> int selectCount(T entity) {
		Assert.isTrue(entity != null, MESSAGE_ENTITY_IS_NULL);
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