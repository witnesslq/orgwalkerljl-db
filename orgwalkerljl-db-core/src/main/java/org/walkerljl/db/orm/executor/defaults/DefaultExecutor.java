package org.walkerljl.db.orm.executor.defaults;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.walkerljl.commons.collection.ArraysUtils;
import org.walkerljl.commons.collection.MapUtils;
import org.walkerljl.db.dbutil.BasicRowProcessor;
import org.walkerljl.db.dbutil.BeanProcessor;
import org.walkerljl.db.dbutil.QueryRunner;
import org.walkerljl.db.dbutil.handler.ArrayHandler;
import org.walkerljl.db.dbutil.handler.ArrayListHandler;
import org.walkerljl.db.dbutil.handler.BeanHandler;
import org.walkerljl.db.dbutil.handler.BeanListHandler;
import org.walkerljl.db.dbutil.handler.BeanMapHandler;
import org.walkerljl.db.dbutil.handler.ColumnListHandler;
import org.walkerljl.db.dbutil.handler.KeyedHandler;
import org.walkerljl.db.dbutil.handler.MapHandler;
import org.walkerljl.db.dbutil.handler.MapListHandler;
import org.walkerljl.db.dbutil.handler.ScalarHandler;
import org.walkerljl.db.orm.DataAccessException;
import org.walkerljl.db.orm.entity.Table;
import org.walkerljl.db.orm.executor.Executor;
import org.walkerljl.db.orm.parse.TableManager;
import org.walkerljl.log.Logger;
import org.walkerljl.log.LoggerFactory;

/**
 * 默认数据访问器 
 *
 * @author lijunlin
 */
public class DefaultExecutor implements Executor {

	private static final String MESSAGE_QUERY_FAILURE = "Query failure";
	private static final String MESSAGE_UPDATE_FAILURE = "Update failure";
	private static final String MESSAGE_INSERT_FAILURE = "Insert failure";
	
    private static final Logger LOG = LoggerFactory.getLogger(DefaultExecutor.class);

    /** 连接池*/
    private static final ThreadLocal<Connection> CONNECTION_POOL = new ThreadLocal<Connection>();
    private final DataSource dataSource;
    private final QueryRunner queryRunner;

    /**
     * 构造函数
     * @param dataSource
     */
    public DefaultExecutor(DataSource dataSource) {
    	this.dataSource = dataSource;
        queryRunner = new QueryRunner(this.dataSource);
    }
    
    @Override
    public <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T result;
        try {
            Map<String, String> columnMap = getColumnToPropertyMap(entityClass);
            if (MapUtils.isNotEmpty(columnMap)) {
                result = queryRunner.query(sql, new BeanHandler<T>(entityClass, new BasicRowProcessor(new BeanProcessor(columnMap))), params);
            } else {
                result = queryRunner.query(sql, new BeanHandler<T>(entityClass), params);
            }
        } catch (SQLException e) {
        	LOG.error(MESSAGE_QUERY_FAILURE, e);
            throw new DataAccessException(e);
        } finally {
        	printSQL(sql);
        }
        return result;
    }

    @Override
    public <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> result;
        try {
            Map<String, String> columnMap = getColumnToPropertyMap(entityClass);
            if (MapUtils.isNotEmpty(columnMap)) {
                result = queryRunner.query(sql, new BeanListHandler<T>(entityClass, new BasicRowProcessor(new BeanProcessor(columnMap))), params);
            } else {
                result = queryRunner.query(sql, new BeanListHandler<T>(entityClass), params);
            }
        } catch (SQLException e) {
        	LOG.error(MESSAGE_QUERY_FAILURE, e);
            throw new DataAccessException(e);
        } finally {
        	printSQL(sql);
        }
        return result;
    }

    @Override
    public <K, V> Map<K, V> queryEntityMap(Class<V> entityClass, String sql, Object... params) {
        Map<K, V> entityMap;
        try {
            entityMap = queryRunner.query(sql, new BeanMapHandler<K, V>(entityClass), params);
        } catch (SQLException e) {
        	LOG.error(MESSAGE_QUERY_FAILURE, e);
            throw new DataAccessException(e);
        } finally {
        	printSQL(sql);
        }
        return entityMap;
    }

    @Override
    public Object[] queryArray(String sql, Object... params) {
        Object[] array;
        try {
            array = queryRunner.query(sql, new ArrayHandler(), params);
        } catch (SQLException e) {
        	LOG.error(MESSAGE_QUERY_FAILURE, e);
            throw new DataAccessException(e);
        } finally {
        	printSQL(sql);
        }
        return array;
    }

    @Override
    public List<Object[]> queryArrayList(String sql, Object... params) {
        List<Object[]> arrayList;
        try {
            arrayList = queryRunner.query(sql, new ArrayListHandler(), params);
        } catch (SQLException e) {
        	LOG.error(MESSAGE_QUERY_FAILURE, e);
            throw new DataAccessException(e);
        } finally {
        	printSQL(sql);
        }
        return arrayList;
    }

    @Override
    public Map<String, Object> queryMap(String sql, Object... params) {
        Map<String, Object> map;
        try {
            map = queryRunner.query(sql, new MapHandler(), params);
        } catch (SQLException e) {
        	LOG.error(MESSAGE_QUERY_FAILURE, e);
            throw new DataAccessException(e);
        } finally {
        	printSQL(sql);
        }
        return map;
    }

    @Override
    public List<Map<String, Object>> queryMapList(String sql, Object... params) {
        List<Map<String, Object>> fieldMapList;
        try {
            fieldMapList = queryRunner.query(sql, new MapListHandler(), params);
        } catch (SQLException e) {
        	LOG.error(MESSAGE_QUERY_FAILURE, e);
            throw new DataAccessException(e);
        } finally {
        	printSQL(sql);
        }
        return fieldMapList;
    }

    @Override
    public <T> T queryColumn(String sql, Object... params) {
        T obj;
        try {
            obj = queryRunner.query(sql, new ScalarHandler<T>(), params);
        } catch (SQLException e) {
        	LOG.error(MESSAGE_QUERY_FAILURE, e);
            throw new RuntimeException(e);
        } finally {
        	printSQL(sql);
        }
        return obj;
    }

    @Override
    public <T> List<T> queryColumnList(String sql, Object... params) {
        List<T> list;
        try {
            list = queryRunner.query(sql, new ColumnListHandler<T>(), params);
        } catch (SQLException e) {
        	LOG.error(MESSAGE_QUERY_FAILURE, e);
            throw new RuntimeException(e);
        } finally {
        	printSQL(sql);
        }
        return list;
    }

    @Override
    public <T> Map<T, Map<String, Object>> queryColumnMap(String column, String sql, Object... params) {
        Map<T, Map<String, Object>> map;
        try {
            map = queryRunner.query(sql, new KeyedHandler<T>(column), params);
        } catch (SQLException e) {
        	LOG.error(MESSAGE_QUERY_FAILURE, e);
            throw new RuntimeException(e);
        } finally {
        	printSQL(sql);
        }
        return map;
    }

    @Override
    public long queryCount(String sql, Object... params) {
        long result;
        try {
            result = queryRunner.query(sql, new ScalarHandler<Long>("count(*)"), params);
        } catch (SQLException e) {
        	LOG.error(MESSAGE_QUERY_FAILURE, e);
            throw new RuntimeException(e);
        } finally {
        	printSQL(sql);
        }
        return result;
    }

    @Override
    public int update(String sql, Object... params) {
        int result;
        try {
            Connection conn = getConnection();
            result = queryRunner.update(conn, sql, params);
        } catch (SQLException e) {
        	LOG.error(MESSAGE_UPDATE_FAILURE, e);
            throw new DataAccessException(e);
        } finally {
        	printSQL(sql);
        }
        return result;
    }

    @Override
    public Serializable insertReturnPK(String sql, Object... params) {
        Serializable key = null;
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            if (ArraysUtils.isNotEmpty(params)) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            int rows = pstmt.executeUpdate();
            if (rows == 1) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    key = (Serializable) rs.getObject(1);
                }
            }
        } catch (SQLException e) {
        	LOG.error(MESSAGE_INSERT_FAILURE, e);
            throw new DataAccessException(e);
        }
        printSQL(sql);
        return key;
    }

    /**
     * 获取字段、属性映射Map
     * @param entityClass
     * @return
     */
    private <T> Map<String, String> getColumnToPropertyMap(Class<T> entityClass) {
    	Table table = TableManager.getInstance().getTable(entityClass);
    	if (table == null) {
    		LOG.warn("Fail to get table by " + entityClass);
    		return null;
    	}
    	return table.getColumnToPropertyMap();
    }

    /**
     * 打印SQL
     * @param sql
     */
    private void printSQL(String sql) {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("[orgwalkerljl-db] SQL - " + sql);
    	}
    }
    
    /**
     * 获取数据库连接
     * @return
     */
    private Connection getConnection() {
        Connection conn;
        try {
            conn = CONNECTION_POOL.get();
            if (conn == null) {
                conn = dataSource.getConnection();
                if (conn != null) {
                    CONNECTION_POOL.set(conn);
                }
            }
        } catch (SQLException e) {
        	LOG.error("Fail to get connection", e);
            throw new RuntimeException(e);
        }
        return conn;
    }
}