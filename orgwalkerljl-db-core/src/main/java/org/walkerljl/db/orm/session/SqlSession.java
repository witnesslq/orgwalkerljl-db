package org.walkerljl.db.orm.session;

import java.io.Closeable;
import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

/**
 * SqlSession
 * 
 * @author lijunlin
 *
 * @param <T>
 * @param <KEY>
 */
public interface SqlSession<T, KEY extends Serializable> extends Closeable {

	 /**
     * 添加对象,返回对象主键
     * @param entity
     * @return
     */
	KEY insertReturnPK(T entity);
	
	/**
	 * 批量添加对象
	 * @param entities
	 * @return
	 */
	int insert(List<T> entities);
    
	/**
	 * 根据主键删除对象
	 * @param entityClass
	 * @param key
	 * @return
	 */
	int deleteByKey(Class<T> entityClass, KEY key);
	
    /**
     * 根据主键列表批量删除对象
     * @param entityClass
     * @param keys
     * @return
     */
	int deleteByKeys(Class<T> entityClass, List<KEY> keys);
    
    /**
     * 删除对象,只要不为NULL与空则为条件
     * @param condition
     * @return
     */
    int delete(T condition);

    /**
     * 根据主键更新对象
     * @param entity
     * @param key
     * @return
     */
    int updateByKey(T entity, KEY key);
    
    /**
     * 根据主键列表更新对象
     * @param entity
     * @param keys
     * @return
     */
    int updateByKeys(T entity, List<KEY> keys);
    
    /**
     * 更新对象
     * @param entity 待更新对象
     * @param condition 更新条件,不为空字段为条件
     * @return
     */
    int update(T entity, T condition);
    
    /**
     * 根据主键获取对象
     * @param entityClass
     * @param key
     * @return
     */
    T selectByKey(Class<T> entityClass, KEY key);
    
    /**
     * 根据主键列表获取对象
     * @param entityClass
     * @param keys
     * @return
     */
    List<T> selectListByKeys(Class<T> entityClass, List<KEY> keys);
    
    /**
     * 查询对象,只要不为NULL与空则为条件
     * @param condition
     * @param currentPage 当前页码
     * @param pageSize 每页大小
     * @return
     */
    List<T> selectList(T condition, int currentPage, int pageSize);
    
    /**
     * 查询对象总数,只要不为NULL与空则为条件
     * @param condition
     * @return
     */
    int selectListCount(T condition);

	void commit();

	void rollback();

	void close();

	Configuration getConfiguration();

	Connection getConnection();
}