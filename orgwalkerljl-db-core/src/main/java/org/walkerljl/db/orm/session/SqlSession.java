package org.walkerljl.db.orm.session;

import java.io.Closeable;
import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

/**
 * SqlSession
 *
 * @author lijunlin
 */
public interface SqlSession extends Closeable {

	<T> int insert(T entity);

	<T> int insert(List<T> entities);
	
	<KEY extends Serializable, T> int deleteByKey(KEY key, T entity);
	
	<KEY extends Serializable, T> int deleteByKeys(List<KEY> keys, T entity);
	
	<T> int delete(T entity);

	<KEY extends Serializable, T> int updateByKey(KEY key, T entity);
	
	<KEY extends Serializable, T> int updateByKeys(List<KEY> keys, T entity);
	
	<T> int update(T entity, T condition);

	<T> List<T> selectOne(T entity);

	<KEY extends Serializable, T> T selectByKey(KEY key, T entity);
	
	<KEY extends Serializable, T> List<T> selectByKeys(List<KEY> keys, T entity);
	
	<T> List<T> selectList(T entity);
	
	<T> int selectCount(T entity);

	void commit();

	void rollback();

	void close();

	Configuration getConfiguration();

	Connection getConnection();
}