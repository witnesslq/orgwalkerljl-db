package org.walkerljl.db.orm.sql;

import java.util.List;

import org.walkerljl.commons.collection.ArraysUtils;
import org.walkerljl.commons.collection.CollectionUtils;
import org.walkerljl.commons.collection.ListUtils;
import org.walkerljl.commons.util.Assert;
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
	
	private static final String MESSAGE_ENTITY_IS_NULL = "实体为null";
	private static final String MESSAGE_ENTITIES_IS_EMPTY = "实体列表为空";
	private static final String MESSAGE_TABLE_IS_NULL = "表为null";
	private static final String MESSAGE_COLUMNS_IS_EMPTY = "字段列表为空";
	private static final String MESSAGE_KEYS_IS_EMPTY = "主键值列表为空";
	private static final String MESSAGE_WHERE_CONDITION_INVALID = "where语句无效";
	private static final String MESSAGE_SET_CLAUSE_INVALID = "set语句无效";
	private static final String MESSAGE_PAGE_PARAMS_INVALID = "分页参数无效";
	private static final String MESSAGE_PRIMARY_KEY_UNDEFINED = "主键未定义";
	
	/**
	 * 生成批量添加实体sql
	 * @param entities
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> SqlEntry generateBatchInsertSql(T... entities) {
		Assert.isTrue(ArraysUtils.isNotEmpty(entities), MESSAGE_ENTITIES_IS_EMPTY);
		Table table = getTable(entities[0]);
		
		int columnsSize = table.getColumns().size();
		String columnsPlaceholder = join("?", columnsSize, ",");
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ").append(table.getName()).append("(").append(table.getColumnNameListString()).append(")VALUES");
		sql.append("(").append(columnsPlaceholder).append(")");
		int entitiesLength = entities.length;
		for (int i = 1; i < entitiesLength; i++) {
			sql.append(",(").append(columnsPlaceholder).append(")");
		}
		
		//参数
		Object[] params = new Object[columnsSize * entitiesLength];
		int index = 0;
		for (int i = 0; i < entitiesLength; i++) {
			Object[] singleEntityParams = EntityFieldValueUtils.getFieldValues(entities[i]);
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
	 * 生成删除实体SQL
	 * @param entityClass
	 * @param keys
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <KEY, T> SqlEntry generateDeleteByKeysSql(Class<T> entityClass, KEY... keys) {
		Assert.isTrue(ArraysUtils.isNotEmpty(keys), MESSAGE_KEYS_IS_EMPTY);
		
		Table table = getTable(entityClass);
		
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
		Table table = getTable(entity);
		
		SqlEntry whereClause = generateWhereClause(table.getColumns(), entity, false);
		
		StringBuilder sql = new StringBuilder("DELETE FROM ").append(table.getName()).append(whereClause.getSql());
		return new SqlEntry(sql.toString(), whereClause.getParams());
	}
	
	/**
	 * 生成更新实体SQL
	 * @param entity
	 * @param keys
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <KEY, T> SqlEntry generateUpdateByKeysSql(T entity, KEY... keys) {
		Assert.isTrue(ArraysUtils.isNotEmpty(keys), MESSAGE_KEYS_IS_EMPTY);
		
		Table table = getTable(entity);
		SqlEntry setClause = generateSetClause(entity, table.getColumns());
		
		//SQL
		int keysLength = keys.length;
		StringBuilder sql = new StringBuilder("UPDATE ").append(table.getName()).append(setClause.getSql()).append(" WHERE ").append(table.getPrimaryKey().getName());
		if (keysLength == 1) {
			sql.append(" = ").append(keys);
		} else {
			sql.append(" IN (").append(join("?", keysLength, ",")).append(")");
		}
		
		//参数
		int setClauseParamsLength = setClause.getParams().length;
		Object[] params = new Object[keysLength + setClauseParamsLength];
		int index = 0;
		for (int i = 0; i < setClauseParamsLength; i++) {
			params[i] = setClause.getParams()[i];
			index ++;
		}
		for (int i = 0; i < keysLength; i++) {
			params[index + i] = keys[i];
		}
		return new SqlEntry(sql.toString(), params);
	}
		
	/**
	 * 更新
	 * @param entity
	 * @param conditionEntity
	 * @return
	 */
	public static <KEY, T> SqlEntry generateUpdateSql(T entity, T conditionEntity) {
		Assert.isTrue(conditionEntity != null, MESSAGE_ENTITY_IS_NULL);
		
		Table table = getTable(entity);
		SqlEntry setClause = generateSetClause(entity, table.getColumns());
		SqlEntry whereClause = generateWhereClause(table.getColumns(), conditionEntity, false);
		
		StringBuilder sql = new StringBuilder("UPDATE ").append(table.getName()).append(setClause.getSql()).append(whereClause.getSql());
		int setClauseParamsLength = setClause.getParams().length;
		int whereClauseParamsLength = whereClause.getParams().length;
		Object[] params = new Object[setClauseParamsLength + whereClauseParamsLength];
		int index = 0;
		for (int i = 0; i < setClauseParamsLength; i++) {
			params[i] = setClause.getParams()[i];
			index ++;
		}
		for (int i = 0; i < whereClauseParamsLength; i++) {
			params[index + i] = whereClause.getParams()[i];	
		}
		return new SqlEntry(sql.toString(), params);
	}
	
	/**
	 * 查询
	 * @param entityClass
	 * @param keys
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <KEY, T> SqlEntry generateSelectByKeysSql(Class<T> entityClass, KEY... keys) {
		Assert.isTrue(ArraysUtils.isNotEmpty(keys), MESSAGE_KEYS_IS_EMPTY);
		
		Table table = getTable(entityClass);
		
		int keysLength = keys.length;
		StringBuilder sql = new StringBuilder("SELECT ").append(table.getColumnNameListString()).append(" FROM ").append(table.getName()).append(" WHERE ").append(table.getPrimaryKey().getName());
		if (keysLength == 1) {
			sql.append(" = ").append("?").append(" LIMIT 0,1");
		} else {
			sql.append(" IN(").append(join("?", keysLength, ",")).append(")");
		}
		return new SqlEntry(sql.toString(), keys);
	}
	
	/**
	 * 查询
	 * @param entity
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public static <KEY, T> SqlEntry generateSelectSql(T entity, int currentPage, int pageSize) {
		Assert.isTrue((currentPage > 0 && pageSize > 0), MESSAGE_PAGE_PARAMS_INVALID);
		
		Table table = getTable(entity);
		SqlEntry whereClause = generateWhereClause(table.getColumns(), entity, true);
		
		Page<T> page = new Page<T>(currentPage, pageSize);
		StringBuilder sql = new StringBuilder("SELECT ").append(table.getColumnNameListString()).append(" FROM ").append(table.getName());
		if (whereClause != null) {
			sql.append(whereClause.getSql());
		}
		sql.append(" LIMIT ").append(page.getStartIndex()).append(",").append(page.getPageSize());
		if (whereClause == null) {
			return new SqlEntry(sql.toString(), null);
		} else {
			return new SqlEntry(sql.toString(), whereClause.getParams());
		}
	}
	
	/**
	 * 查询
	 * @param entity
	 * @return
	 */
	public static <KEY, T> SqlEntry generateSelectCountSql(T entity) {
		Assert.isTrue(entity != null, MESSAGE_ENTITY_IS_NULL);
		
		Table table = getTable(entity);
		SqlEntry whereClause = generateWhereClause(table.getColumns(), entity, true);
		
		StringBuilder sql = new StringBuilder("SELECT COUNT(*)").append(" FROM ").append(table.getName());
		if (whereClause != null) {
			sql.append(whereClause.getSql());
		}
		
		if (whereClause == null) {
			return new SqlEntry(sql.toString(), null);
		} else {
			return new SqlEntry(sql.toString(), whereClause.getParams());
		}
	}
	
	/**
	 * 生成Where语句
	 * @param columns
	 * @param entity
	 * @param allowInvalid
	 * @return
	 */
	private static <T> SqlEntry generateWhereClause(List<Column> columns, T entity, boolean allowInvalid) {
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
			} else {
				whereCondition.append(" AND ");
			}
			whereCondition.append(column.getName()).append(" = ?");			
			params.add(entityFieldValue);
			counter ++;
		}
		
		//校验
		if (allowInvalid) {
			if (CollectionUtils.isEmpty(params)) {
				return null;
			}
		} else {
			Assert.isTrue(CollectionUtils.isNotEmpty(params), MESSAGE_WHERE_CONDITION_INVALID);
		}
		
		Object[] paramsArray = new Object[params.size()];
		return new SqlEntry(whereCondition.toString(), params.toArray(paramsArray));
    }
	
	/**
	 * 生成SET 语句
	 * @param entity
	 * @param columns
	 * @return
	 */
	private static <T> SqlEntry generateSetClause(T entity, List<Column> columns) {
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
			} else {
				setClause.append(",");
			}
			setClause.append(column.getName()).append(" = ?");			
			params.add(entityFieldValue);
			counter ++;
		}

		//校验
		Assert.isTrue(CollectionUtils.isNotEmpty(params), MESSAGE_SET_CLAUSE_INVALID);
		
		Object[] paramsArray = new Object[]{counter};
		return new SqlEntry(setClause.toString(), params.toArray(paramsArray));
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
	 * 获取Table
	 * @param entity
	 * @return
	 */
	private static <T> Table getTable(T entity) {
		//校验实体对象是否有效
		Assert.isTrue(entity != null, MESSAGE_ENTITY_IS_NULL);
		
		return getTable(entity.getClass());
	}
	
	/**
	 * 获取Table
	 * @param entityClass
	 * @return
	 */
	private static <T> Table getTable(Class<T> entityClass) {
		//校验根据实体是否能得到表对象
		Table table = TableManager.getInstance().getTable(entityClass);
		Assert.isTrue(table != null, MESSAGE_TABLE_IS_NULL);
		
		//校验字段列表是否为空
		Assert.isTrue(CollectionUtils.size(table.getColumns()) != 0, MESSAGE_COLUMNS_IS_EMPTY);
		
		//校验主键是否存在
		Assert.isTrue(table.getPrimaryKey() != null, MESSAGE_PRIMARY_KEY_UNDEFINED);
		
		return table;
	}
}