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

	/**
	 * 添加
	 * @param entities
	 * @return
	 */
	@SuppressWarnings("unchecked")
	<T> int insert(T... entities);
	
	/**
	 * 根据主键Key列表批量删除
	 * @param entityClass
	 * @param keys
	 * @return
	 */
	@SuppressWarnings("unchecked")
	<KEY extends Serializable, T> int deleteByKeys(Class<T> entityClass, KEY... keys);
	
	/**
	 * 删除
	 * @param entity
	 * @return
	 */
	<T> int delete(T entity);
	
	/**
	 * 根据主键列表批量更新
	 * @param entity
	 * @param keys
	 * @return
	 */
	@SuppressWarnings("unchecked")
	<KEY extends Serializable, T> int updateByKeys(T entity, KEY... keys);
	
	/**
	 * 更新
	 * @param entity
	 * @param condition
	 * @return
	 */
	<T> int update(T entity, T condition);
	
	/**
	 * 根据主键列表查询
	 * @param entityClass
	 * @param keys
	 * @return
	 */
	@SuppressWarnings("unchecked")
	<KEY extends Serializable, T> List<T> selectByKeys(Class<T> entityClass, KEY... keys);
	
	/**
	 * 查询单个实体
	 * @param entity
	 * @return
	 */
	<T> T selectOne(T entity);
	
	/**
	 * 查询实体列表
	 * @param entity
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	<T> List<T> selectList(T entity, int currentPage, int pageSize);
	
	/**
	 * 查询实体数量
	 * @param entity
	 * @return
	 */
	<T> int selectCount(T entity);

	void commit();

	void rollback();

	void close();

	Configuration getConfiguration();

	Connection getConnection();
}