package org.walkerljl.db.orm.sql;

import java.util.List;

import org.walkerljl.commons.collection.CollectionUtils;
import org.walkerljl.commons.collection.ListUtils;
import org.walkerljl.commons.util.StringPool;
import org.walkerljl.commons.util.StringUtils;
import org.walkerljl.db.dbutil.DbUtils;
import org.walkerljl.db.orm.EntityFieldValueUtils;
import org.walkerljl.db.orm.Page;
import org.walkerljl.db.orm.entity.Column;
import org.walkerljl.db.orm.entity.Table;
import org.walkerljl.db.orm.parse.TableManager;
import org.walkerljl.log.Logger;
import org.walkerljl.log.LoggerFactory;

/**
 * 
 * SQL 生成工具类
 *
 * @author lijunlin
 */
public class SqlGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(SqlGenerator.class);
	
	public static <T> SqlEntry generateInsertSql(T entity) {
		if (entity == null) {
			return null;
		}
		Table table = TableManager.getInstance().getTable(entity.getClass());
		List<Column> columns = null;
		if (table == null || CollectionUtils.isEmpty((columns = table.getColumns()))) {
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ").append(table.getName()).append("(").append(table.getColumnNameListString());
		sql.append(")VALUES(").append(join("?", columns.size(), ",")).append(")");
		return new SqlEntry(sql.toString(), EntityFieldValueUtils.getFieldValues(entity));
	}
	
	public static <T> SqlEntry generateBatchInsertSql(List<T> entites) {
		if (ListUtils.isEmpty(entites)) {
			return null;
		}
		Table table = TableManager.getInstance().getTable(entites.get(0).getClass());
		List<Column> columns = null;
		if (table == null || CollectionUtils.isEmpty((columns = table.getColumns()))) {
			return null;
		}
		
		int columnSize = columns.size();
		Object[] params = new Object[columnSize * entites.size()];
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ").append(table.getName()).append("(").append(table.getColumnNameListString()).append(")VALUES(");
		sql.append(join("?", columnSize, ",")).append(")");
		for (int i = 1; i < entites.size(); i++) {
			sql.append(",(").append(join("?", columnSize, ",")).append(")");
		}
		//return new SqlEntry(sql.toString(), EntityFieldValueUtils.getFieldValues(entity));
		return null;
	}
	
	public static <KEY, T> SqlEntry generateDeleteByKeySql(KEY key, T entity) {
		if (entity == null) {
			return null;
		}
		Table table = TableManager.getInstance().getTable(entity.getClass());
		if (table == null) {
			return null;
		}
		StringBuilder sql = new StringBuilder("DELETE FROM ").append(table.getName());
		String wrap = getWrap(table);
		sql.append(" WHERE ").append(table.getPrimaryKey().getName()).append(" = ").append(wrap).append("?").append(wrap);
		return new SqlEntry(sql.toString(), new Object[]{key});
	}
	
	private static String getWrap(Table table) {
		return table.getPrimaryKey().isNumeric() ? "" : "'";
	}
	
	private static String join(String text, int count, String seperator) {
		StringBuilder buffer = new StringBuilder(text.length() * count);
		buffer.append(text);
		for (int i = 1; i < count; i++) {
			buffer.append(seperator).append(text);
		}
		return buffer.toString();
	}
	
	public static <KEY, T> SqlEntry generateDeleteByKeysSql(List<KEY> keys, T entity) {
		if (entity == null) {
			return null;
		}
		Table table = TableManager.getInstance().getTable(entity.getClass());
		List<Column> columns = null;
		if (table == null || CollectionUtils.isEmpty((columns = table.getColumns()))) {
			return null;
		}
		String wrap = getWrap(table);
		StringBuilder sql = new StringBuilder("DELETE FROM ").append(table.getName());
		sql.append(" WHERE ").append(table.getPrimaryKey().getName()).append(" IN (").append(wrap).append(join("'", columns.size(), ",")).append(")");
		return new SqlEntry(sql.toString(), EntityFieldValueUtils.getFieldValues(entity));
	}
	
	public static <KEY, T> SqlEntry generateDeleteSql(T entity) {
		if (entity == null) {
			return null;
		}
		Table table = TableManager.getInstance().getTable(entity.getClass());
		List<Column> columns = null;
		if (table == null || CollectionUtils.isEmpty((columns = table.getColumns()))) {
			return null;
		}
		SqlEntry whereSqlEntry = generateWhere(columns, entity);
		StringBuilder sql = new StringBuilder("DELETE FROM ").append(table.getName()).append(whereSqlEntry.getSql());
		return new SqlEntry(sql.toString(), whereSqlEntry.getParams());
	}
	
	public static <KEY, T> SqlEntry generateUpdateByKeySql(List<KEY> keys, T entity) {
		if (ListUtils.isEmpty(keys) || entity == null) {
			return null;
		}
		Table table = TableManager.getInstance().getTable(entity.getClass());
		List<Column> columns = null;
		if (table == null || CollectionUtils.isEmpty((columns = table.getColumns()))) {
			return null;
		}
		StringBuilder setClause = new StringBuilder(" SET ");
		List<Object> params = ListUtils.newArrayList();
		int counter = 0;
		for (Column column : columns) {
			Object entityFieldValue = EntityFieldValueUtils.getFieldValue(entity, column);
			if (entityFieldValue == null) {
				continue;
			}
			if (counter != 0) {
				setClause.append(",");
			}
			setClause.append(column.getName()).append(" = ");
			boolean isNotNumeric = !column.isNumeric();
			if (isNotNumeric) {
				setClause.append("'");
			}
			setClause.append(entityFieldValue);
			if (isNotNumeric) {
				setClause.append("'");
			}
			params.add(entityFieldValue);
			counter ++;
		}
		int keySize = keys.size();
		StringBuilder sql = new StringBuilder("UPDATE ").append(table.getName()).append(setClause);
		sql.append(" WHERE ").append(table.getPrimaryKey().getName()).append(" IN (").append(join("?", keys.size(), ",")).append(")");
		//return new SqlEntry(sql.toString(), params);
		return null;
	}
	
	public static <KEY, T> SqlEntry generateUpdateSql(T entity, T conditionEntity) {
		if (entity == null) {
			return null;
		}
		Table table = TableManager.getInstance().getTable(entity.getClass());
		List<Column> columns = null;
		if (table == null || CollectionUtils.isEmpty((columns = table.getColumns()))) {
			return null;
		}
		StringBuilder setClause = new StringBuilder(" SET ");
		List<Object> params = ListUtils.newArrayList();
		int counter = 0;
		for (Column column : columns) {
			Object entityFieldValue = EntityFieldValueUtils.getFieldValue(entity, column);
			if (entityFieldValue == null) {
				continue;
			}
			if (counter != 0) {
				setClause.append(",");
			}
			setClause.append(column.getName()).append(" = ");
			boolean isNotNumeric = !column.isNumeric();
			if (isNotNumeric) {
				setClause.append("'");
			}
			setClause.append(entityFieldValue);
			if (isNotNumeric) {
				setClause.append("'");
			}
			params.add(entityFieldValue);
			counter ++;
		}
		SqlEntry whereSqlEntry = generateWhere(columns, entity);
		StringBuilder sql = new StringBuilder("UPDATE ").append(table.getName()).append(setClause).append(generateWhere(columns, entity));
		return null;
	}
	
	public static <KEY, T> SqlEntry generateSelectByKeySql(KEY key, T entity) {
		if (entity == null) {
			return null;
		}
		Table table = TableManager.getInstance().getTable(entity.getClass());
		if (table == null || table.getPrimaryKey() == null) {
			return null;
		}
		String wrap = getWrap(table);
		StringBuilder sql = new StringBuilder("SELECT ").append(table.getColumnNameListString()).append(" FROM ").append(table.getName());
		sql.append(" WHERE ").append(table.getPrimaryKey().getName()).append(" = ").append(wrap).append(key).append(wrap);
		sql.append(" LIMIT 0,1");
		return null;
	}
	
	public static <KEY, T> SqlEntry generateSelectByKeysSql(List<KEY> keys, T entity) {
		if (ListUtils.isEmpty(keys) || entity == null) {
			return null;
		}
		Table table = TableManager.getInstance().getTable(entity.getClass());
		if (table == null || table.getPrimaryKey() == null) {
			return null;
		}
		int keySize = keys.size();
		StringBuilder sql = new StringBuilder("SELECT ").append(table.getColumnNameListString()).append(" FROM ").append(table.getName());
		sql.append(" WHERE ").append(table.getPrimaryKey().getName()).append(" IN ").append(join("?", keySize, "'"));
		Object[] params = new Object[]{keySize};
		return new SqlEntry(sql.toString(), keys.toArray(params));
	}
	
	public static <KEY, T> String generateSelectSql(T entity, int currentPage, int pageSize) {
		if (entity == null) {
			return StringPool.EMPTY;
		}
		Table table = TableManager.getInstance().getTable(entity.getClass());
		List<Column> columns = null;
		if (table == null || CollectionUtils.isEmpty((columns = table.getColumns()))) {
			return null;
		}
		Page<T> page = new Page<T>(currentPage, pageSize);
		StringBuilder sql = new StringBuilder("SELECT ").append(table.getColumnNameListString()).append(" FROM ").append(table.getName()).append(generateWhere(columns, entity));
		sql.append(" LIMIT ").append(page.getStartIndex()).append(", ").append(page.getPageSize());
		return sql.toString();
	}
	
	public static <KEY, T> String generateSelectCountSql(T entity, int currentPage, int pageSize) {
		if (entity == null) {
			return StringPool.EMPTY;
		}
		Table table = TableManager.getInstance().getTable(entity.getClass());
		List<Column> columns = null;
		if (table == null || CollectionUtils.isEmpty((columns = table.getColumns()))) {
			return null;
		}
		StringBuilder sql = new StringBuilder("SELECT COUNT(*) ").append(" FROM ").append(table.getName()).append(generateWhere(columns, entity));
		return sql.toString();
	}
	
	private static <T> SqlEntry generateWhere(List<Column> columns, T entity) {
		StringBuilder whereCondition = new StringBuilder(" WHERE ");
		List<Object> params = ListUtils.newArrayList();
		int counter = 0;
		for (Column column : columns) {
			Object entityFieldValue = EntityFieldValueUtils.getFieldValue(entity, column);
			if (entityFieldValue == null) {
				continue;
			}
			if (counter != 0) {
				whereCondition.append(" AND ");
			}
			whereCondition.append(column.getName()).append(" = ");
			boolean isNotNumeric = !column.isNumeric();
			if (isNotNumeric) {
				whereCondition.append("'");
			}
			whereCondition.append(entityFieldValue);
			if (isNotNumeric) {
				whereCondition.append("'");
			}
			
			params.add(entityFieldValue);
			counter ++;
		}
		Object[] paramsArray = new Object[params.size()];
		return new SqlEntry(whereCondition.toString(), params.toArray(paramsArray));
    }
	
    /**
     * 生成 select count(*) 语句
     */
    public static String generateSelectSqlForCount(Class<?> entityClass, String condition) {
        StringBuilder sql = new StringBuilder("select count(1) from ").append(getTable(entityClass).getName());
        sql.append(generateWhere(condition));
        return sql.toString();
    }

    /**
     * 生成 select 分页语句（数据库类型为：mysql、oracle、mssql）
     */
    public static String generateSelectSqlForPager(int pageNumber, int pageSize, Object entity, String condition, String sort) {
        StringBuilder sql = new StringBuilder();
        String table = getTable(entity).getName();
        String where = generateWhere(condition);
        String order = generateOrder(sort);
        String dbType = null;//DbUtils.getDatabaseType();
        if (dbType.equalsIgnoreCase("mysql")) {
            int pageStart = (pageNumber - 1) * pageSize;
            appendSqlForMySql(sql, table, where, order, pageStart, pageSize);
        } else if (dbType.equalsIgnoreCase("oracle")) {
            int pageStart = (pageNumber - 1) * pageSize + 1;
            int pageEnd = pageStart + pageSize;
            appendSqlForOracle(sql, table, where, order, pageStart, pageEnd);
        } else if (dbType.equalsIgnoreCase("mssql")) {
            int pageStart = (pageNumber - 1) * pageSize;
            appendSqlForMsSql(sql, table, where, order, pageStart, pageSize);
        }
        return sql.toString();
    }

    private static Table getTable(Object entity) {
    	Table table = TableManager.getInstance().getTable(entity.getClass());
    	if (table == null) {
    		String errorMessage = String.format("Get null table object by entity : %s", entity.getClass());
    		LOGGER.error(errorMessage);
    		throw new SqlGenerateException(errorMessage);
    	}
    	return table;
    }

    private static String generateWhere(String condition) {
        String where = "";
        if (StringUtils.isNotEmpty(condition)) {
            where += " where " + condition;
        }
        return where;
    }

    private static String generateOrder(String sort) {
        String order = "";
        if (StringUtils.isNotEmpty(sort)) {
            order += " order by " + sort;
        }
        return order;
    }

    private static void appendSqlForMySql(StringBuilder sql, String table, String where, String order, int pageStart, int pageEnd) {
        /*
            select * from 表名 where 条件 order by 排序 limit 开始位置, 结束位置
         */
        sql.append("select * from ").append(table);
        sql.append(where);
        sql.append(order);
        sql.append(" limit ").append(pageStart).append(", ").append(pageEnd);
    }

    private static void appendSqlForOracle(StringBuilder sql, String table, String where, String order, int pageStart, int pageEnd) {
        /*
            select a.* from (
                select rownum rn, t.* from 表名 t where 条件 order by 排序
            ) a
            where a.rn >= 开始位置 and a.rn < 结束位置
        */
        sql.append("select a.* from (select rownum rn, t.* from ").append(table).append(" t");
        sql.append(where);
        sql.append(order);
        sql.append(") a where a.rn >= ").append(pageStart).append(" and a.rn < ").append(pageEnd);
    }

    private static void appendSqlForMsSql(StringBuilder sql, String table, String where, String order, int pageStart, int pageEnd) {
        /*
            select top 结束位置 * from 表名 where 条件 and id not in (
                select top 开始位置 id from 表名 where 条件 order by 排序
            ) order by 排序
        */
        sql.append("select top ").append(pageEnd).append(" * from ").append(table);
        if (StringUtils.isNotEmpty(where)) {
            sql.append(where).append(" and ");
        } else {
            sql.append(" where ");
        }
        sql.append("id not in (select top ").append(pageStart).append(" id from ").append(table);
        sql.append(where);
        sql.append(order);
        sql.append(") ").append(order);
    }
}