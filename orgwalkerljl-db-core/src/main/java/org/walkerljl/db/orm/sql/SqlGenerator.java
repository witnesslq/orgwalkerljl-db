package org.walkerljl.db.orm.sql;

import java.util.List;

import org.walkerljl.commons.collection.ArraysUtils;
import org.walkerljl.commons.collection.CollectionUtils;
import org.walkerljl.commons.collection.ListUtils;
import org.walkerljl.db.orm.EntityFieldValueUtils;
import org.walkerljl.db.orm.Page;
import org.walkerljl.db.orm.entity.Column;
import org.walkerljl.db.orm.entity.SqlEntry;
import org.walkerljl.db.orm.entity.Table;
import org.walkerljl.db.orm.parse.TableManager;

/**
 * 
 * SQL 生成工具类
 *
 * @author lijunlin
 */
public class SqlGenerator {
	
	/**
	 * 生成添加实体SQL
	 * @param entity
	 * @return
	 */
	public static <T> SqlEntry generateInsertSql(T entity) {
		if (entity == null) {
			return null;
		}
		Table table = TableManager.getInstance().getTable(entity.getClass());
		int columnsSize = CollectionUtils.size(table.getColumns());
		if (table == null || columnsSize == 0) {
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ").append(table.getName()).append("(").append(table.getColumnNameListString());
		sql.append(")VALUES(").append(join("?", columnsSize, ",")).append(")");
		return new SqlEntry(sql.toString(), EntityFieldValueUtils.getFieldValues(entity));
	}
	
	/**
	 * 生成批量添加实体sql
	 * @param entities
	 * @return
	 */
	public static <T> SqlEntry generateBatchInsertSql(List<T> entities) {
		if (ListUtils.isEmpty(entities)) {
			return null;
		}
		Table table = TableManager.getInstance().getTable(entities.get(0).getClass());
		int columnsSize = CollectionUtils.size(table.getColumns());
		if (table == null || columnsSize == 0) {
			return null;
		}
		
		String columnsPlaceholder = join("?", columnsSize, ",");
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ").append(table.getName()).append("(").append(table.getColumnNameListString()).append(")VALUES");
		sql.append("(").append(columnsPlaceholder).append(")");
		for (int i = 1; i < entities.size(); i++) {
			sql.append(",(").append(columnsPlaceholder).append(")");
		}
		
		//参数
		Object[] params = new Object[columnsSize * entities.size()];
		int index = 0;
		for (int i = 0; i < entities.size(); i++) {
			Object[] singleEntityParams = EntityFieldValueUtils.getFieldValues(entities.get(i));
			if (singleEntityParams == null) {
				index += columnsSize - 1;
				continue;
			}
			for (int j = 0; j < columnsSize; j++) {
				params[index] = singleEntityParams[j];
				index ++;
			}
		}
		return new SqlEntry(sql.toString(), params);
	}
	
	/**
	 * 拼接字符串
	 * @param text
	 * @param count
	 * @param seperator
	 * @return
	 */
	private static String join(String text, int count, String seperator) {
		StringBuilder buffer = new StringBuilder(text.length() * count);
		buffer.append(text);
		for (int i = 1; i < count; i++) {
			buffer.append(seperator).append(text);
		}
		return buffer.toString();
	}
	
	/**
	 * 生成删除实体SQL
	 * @param entity
	 * @param keys
	 * @return
	 */
	public static <KEY, T> SqlEntry generateDeleteByKeysSql(T entity, KEY... keys) {
		if (entity == null || ArraysUtils.isEmpty(keys)) {
			return null;
		}
		Table table = TableManager.getInstance().getTable(entity.getClass());
		if (table == null || CollectionUtils.isEmpty(table.getColumns())) {
			return null;
		}
		StringBuilder sql = new StringBuilder("DELETE FROM ").append(table.getName()).append(" WHERE ").append(table.getPrimaryKey().getName());
		int keysLength = keys.length;
		if (keysLength == 1) {
			sql.append(" = ").append("?");
		} else {
			sql.append(" IN (").append(join("?", keysLength, ",")).append(")");
		}
		return new SqlEntry(sql.toString(), keys);
	}
	
	/**
	 * 生成删除实体SQL
	 * @param entity
	 * @return
	 */
	public static <KEY, T> SqlEntry generateDeleteSql(T entity) {
		if (entity == null) {
			return null;
		}
		Table table = TableManager.getInstance().getTable(entity.getClass());
		if (table == null || CollectionUtils.isEmpty(table.getColumns())) {
			return null;
		}
		SqlEntry whereSqlEntry = generateWhere(table.getColumns(), entity);
		if (whereSqlEntry == null || ArraysUtils.isEmpty(whereSqlEntry.getParams())) {
			return null;
		}
		StringBuilder sql = new StringBuilder("DELETE FROM ").append(table.getName()).append(whereSqlEntry.getSql());
		return new SqlEntry(sql.toString(), whereSqlEntry.getParams());
	}
	
	/**
	 * 生成更新实体SQL
	 * @param entity
	 * @param keys
	 * @return
	 */
	public static <KEY, T> SqlEntry generateUpdateByKeysSql(T entity, KEY... keys) {
		if (ArraysUtils.isEmpty(keys) || entity == null) {
			return null;
		}
		Table table = TableManager.getInstance().getTable(entity.getClass());
		if (table == null || CollectionUtils.isEmpty(table.getColumns())) {
			return null;
		}
		SqlEntry setClauseSqlEntry = getSetClause(entity, table.getColumns());
		if (setClauseSqlEntry == null || ArraysUtils.isEmpty(setClauseSqlEntry.getParams())) {
			return null;
		}
		int keysLength = keys.length;
		StringBuilder sql = new StringBuilder("UPDATE ").append(table.getName()).append(setClauseSqlEntry.getSql()).append(" WHERE ").append(table.getPrimaryKey().getName());
		if (keysLength == 1) {
			sql.append(" = ").append(keys);
		} else {
			sql.append(" IN (").append(join("?", keysLength, ",")).append(")");
		}
		int setClauseParamsLength = (setClauseSqlEntry.getParams() == null ? 0 : setClauseSqlEntry.getParams().length);
		int paramsLength = setClauseParamsLength + keysLength;
		Object[] params = new Object[paramsLength];
		int index = -1;
		for (int i = 0; i < setClauseParamsLength; i++) {
			params[i] = setClauseSqlEntry.getParams()[i];
			index ++;
		}
		if (index == -1) {
			index = 0;
		}
		for (int i = 0; i < keysLength; i++) {
			params[index + i] = keys[i];
		}
		return new SqlEntry(sql.toString(), params);
	}
	
	private static <T> SqlEntry getSetClause(T entity, List<Column> columns) {
		StringBuilder setClause = new StringBuilder();
		int counter = 0;
		List<Object> params = ListUtils.newArrayList();
		for (Column column : columns) {
			Object entityFieldValue = EntityFieldValueUtils.getFieldValue(entity, column);
			if (entityFieldValue == null) {
				continue;
			}
			if (counter == 0) {
				setClause.append(" SET ");
			}
			if (counter != 0) {
				setClause.append(",");
			}
			setClause.append(column.getName()).append(" = ?");			
			params.add(entityFieldValue);
			counter ++;
		}
		if (CollectionUtils.isEmpty(params)) {
			return null;
		}
		Object[] paramsArray = new Object[]{counter};
		return new SqlEntry(setClause.toString(), params.toArray(paramsArray));
	}
	
	public static <KEY, T> SqlEntry generateUpdateSql(T entity, T conditionEntity) {
		if (entity == null || conditionEntity == null) {
			return null;
		}
		Table table = TableManager.getInstance().getTable(entity.getClass());
		int columnsSize = table.getColumns().size();
		if (table == null || columnsSize == 0) {
			return null;
		}
		SqlEntry setClauseSqlEntry = getSetClause(entity, table.getColumns());
		if (setClauseSqlEntry == null || ArraysUtils.isEmpty(setClauseSqlEntry.getParams())) {
			return null;
		}
		SqlEntry whereSqlEntry = generateWhere(table.getColumns(), conditionEntity);
		if (whereSqlEntry == null || ArraysUtils.isEmpty(whereSqlEntry.getParams())) {
			return null;
		}
		StringBuilder sql = new StringBuilder("UPDATE ").append(table.getName()).append(setClauseSqlEntry.getSql()).append(whereSqlEntry.getSql());
		int whereParamsLength = whereSqlEntry.getParams().length;
		int paramsLength = columnsSize + whereParamsLength;
		Object[] params = new Object[]{paramsLength};
		int index = 0;
		for (; index < setClauseSqlEntry.getParams().length; index++) {
			params[index] = setClauseSqlEntry.getParams()[index];
		}
		for (int i = 0; i < whereSqlEntry.getParams().length; i++) {
			params[index + i] = whereSqlEntry.getParams()[i];	
		}
		return new SqlEntry(sql.toString(), params);
	}
	
	public static <KEY, T> SqlEntry generateSelectByKeysSql(T entity, KEY... keys) {
		if (ArraysUtils.isEmpty(keys) || entity == null) {
			return null;
		}
		Table table = TableManager.getInstance().getTable(entity.getClass());
		if (table == null || table.getPrimaryKey() == null) {
			return null;
		}
		int keysLength = keys.length;
		StringBuilder sql = new StringBuilder("SELECT ").append(table.getColumnNameListString()).append(" FROM ").append(table.getName()).append(" WHERE ").append(table.getPrimaryKey().getName());
		if (keysLength == 1) {
			sql.append(table.getPrimaryKey().getName()).append(" = ").append("?").append(" LIMIT 0,1");
		} else {
			sql.append(" IN(").append(join("?", keysLength, ",")).append(")");
		}
		return new SqlEntry(sql.toString(), keys);
	}
	
	public static <KEY, T> SqlEntry generateSelectSql(T entity, int currentPage, int pageSize) {
		if (entity == null || currentPage <= 0 || pageSize <= 0) {
			return null;
		}
		Table table = TableManager.getInstance().getTable(entity.getClass());
		if (table == null || CollectionUtils.isEmpty(table.getColumns())) {
			return null;
		}
		SqlEntry whereSqlEntry = generateWhere(table.getColumns(), entity);
		if (whereSqlEntry == null || ArraysUtils.isEmpty(whereSqlEntry.getParams())) {
			return null;
		}
		Page<T> page = new Page<T>(currentPage, pageSize);
		StringBuilder sql = new StringBuilder("SELECT ").append(table.getColumnNameListString()).append(" FROM ").append(table.getName()).append(whereSqlEntry.getSql());
		sql.append(" LIMIT ").append(page.getStartIndex()).append(",").append(page.getPageSize());
		return new SqlEntry(sql.toString(), whereSqlEntry.getParams());
	}
	
	public static <KEY, T> SqlEntry generateSelectCountSql(T entity) {
		if (entity == null) {
			return null;
		}
		Table table = TableManager.getInstance().getTable(entity.getClass());
		if (table == null || CollectionUtils.isEmpty(table.getColumns())) {
			return null;
		}
		SqlEntry whereSqlEntry = generateWhere(table.getColumns(), entity);
		if (whereSqlEntry == null || ArraysUtils.isEmpty(whereSqlEntry.getParams())) {
			return null;
		}
		StringBuilder sql = new StringBuilder("SELECT COUNT(*) ").append(" FROM ").append(table.getName()).append(whereSqlEntry);
		return new SqlEntry(sql.toString(), whereSqlEntry.getParams());
	}
	
	private static <T> SqlEntry generateWhere(List<Column> columns, T entity) {
		StringBuilder whereCondition = new StringBuilder();
		List<Object> params = ListUtils.newArrayList();
		int counter = 0;
		for (Column column : columns) {
			Object entityFieldValue = EntityFieldValueUtils.getFieldValue(entity, column);
			if (entityFieldValue == null) {
				continue;
			} 
			if (counter == 0) {
				whereCondition.append(" WHERE ");
			}
			if (counter != 0) {
				whereCondition.append(" AND ");
			}
			whereCondition.append(column.getName()).append(" = ?");			
			params.add(entityFieldValue);
			counter ++;
		}
		if (CollectionUtils.isEmpty(params)) {
			return null;
		}
		Object[] paramsArray = new Object[params.size()];
		return new SqlEntry(whereCondition.toString(), params.toArray(paramsArray));
    }
}