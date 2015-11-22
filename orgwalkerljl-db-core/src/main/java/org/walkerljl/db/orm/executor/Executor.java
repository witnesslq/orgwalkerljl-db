package org.walkerljl.db.orm.executor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 数据访问器 
 *
 * @author lijunlin
 */
public interface Executor {

	/**
	 * 查询对应的实体，返回单条记录
	 * @param entityClass
	 * @param sql
	 * @param params
	 * @return
	 */
    <T> T queryEntity(Class<T> entityClass, String sql, Object... params);

    /**
     * 查询对应的实体列表，返回多条记录
     * @param entityClass
     * @param sql
     * @param params
     * @return
     */
    <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params);

    /**
     * 查询对应的实体列表，返回单条记录（主键 => 实体）
     * @param entityClass
     * @param sql
     * @param params
     * @return
     */
    <K, V> Map<K, V> queryEntityMap(Class<V> entityClass, String sql, Object... params);

    /**
     * 查询对应的数据，返回单条记录
     * @param sql
     * @param params
     * @return
     */
    Object[] queryArray(String sql, Object... params);

    /**
     * 查询对应的数据，返回多条记录
     * @param sql
     * @param params
     * @return
     */
    List<Object[]> queryArrayList(String sql, Object... params);

    /**
     * 查询对应的数据，返回单条记录（列名 => 数据）
     * @param sql
     * @param params
     * @return
     */
    Map<String, Object> queryMap(String sql, Object... params);

    /**
     * 查询对应的数据，返回多条记录（列名 => 数据）
     * @param sql
     * @param params
     * @return
     */
    List<Map<String, Object>> queryMapList(String sql, Object... params);

    /**
     * 查询对应的数据，返回单条数据（列名 => 数据）
     * @param sql
     * @param params
     * @return
     */
    <T> T queryColumn(String sql, Object... params);

    /**
     * 查询对应的数据，返回多条数据（列名 => 数据）
     * @param sql
     * @param params
     * @return
     */
    <T> List<T> queryColumnList(String sql, Object... params);

    /**
     * 查询指定列名对应的数据，返回多条数据（列名对应的数据 => 列名与数据的映射关系）
     * @param column
     * @param sql
     * @param params
     * @return
     */
    <T> Map<T, Map<String, Object>> queryColumnMap(String column, String sql, Object... params);

    /**
     * 查询记录条数，返回总记录数
     * @param sql
     * @param params
     * @return
     */
    long queryCount(String sql, Object... params);

    /**
     * 执行更新操作（包括：update、insert、delete），返回所更新的记录数
     * @param sql
     * @param params
     * @return
     */
    int update(String sql, Object... params);

    /**
     * 插入一条记录，返回插入后的主键
     * @param sql
     * @param params
     * @return
     */
    Serializable insertReturnPK(String sql, Object... params);
}