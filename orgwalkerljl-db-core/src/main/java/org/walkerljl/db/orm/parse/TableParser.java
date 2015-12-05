package org.walkerljl.db.orm.parse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.walkerljl.commons.collection.CollectionUtils;
import org.walkerljl.db.api.annotation.Entity;
import org.walkerljl.db.orm.entity.Column;
import org.walkerljl.db.orm.entity.Table;
import org.walkerljl.log.Logger;
import org.walkerljl.log.LoggerFactory;

/**
 *
 * TableParser
 *
 * @author lijunlin
 */
public class TableParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(TableParser.class);
	private static final String KEY_WRAP = "`";
	
	/**
	 * 解析
	 * @param entityClass
	 * @return
	 */
	public static Table parse(Class<?> entityClass) {
		return parseTable(entityClass);
	}
	
	/**
	 * 解析表名
	 * @param entityClass
	 * @return
	 */
	public static String parseTableName(Class<?> entityClass) {
		Table table = parseSimpleTable(entityClass);
		if (table == null) {
			return null;
		}
		return table.getName();
	}
	
	/**
	 * 解析表
	 * @param entityClass
	 * @return
	 */
	private static Table parseTable(Class<?> entityClass) {
		Table table = parseSimpleTable(entityClass);
		if (table == null) {
			return null;
		}
		
		List<Class<?>> supperClasses = getAllSupperClasses(entityClass);
		if (CollectionUtils.isNotEmpty(supperClasses)) {
			for (Class<?> supperClass : supperClasses) {
				parseColumn(table, supperClass);
			}
		}
		parseColumn(table, entityClass);
		return table;
	}
	
	/**
	 * 解析简单表(不包含字段)
	 * @param entityClass
	 * @return
	 */
	private static Table parseSimpleTable(Class<?> entityClass) {
		if (entityClass == null) {
			return null;
		}
		Entity entity = entityClass.getAnnotation(Entity.class);
		if (entity == null) {
			LOGGER.warn(String.format("%s实体没有标注%s注解", entityClass, Table.class));
			return null;
		}
		String tableName = "";
		if ("".equals(entity.value())) {
			tableName = entityClass.getSimpleName();
		} else {
			tableName = entity.value();
		}
		
		Table table = new Table();
		table.setName(KEY_WRAP + tableName + KEY_WRAP);
		table.setComment(entity.comment());
		return table;
	}
	
	/**
	 * 解析字段
	 * @param table
	 * @param entityClass
	 */
	private static void parseColumn(Table table, Class<?> entityClass) {
		if (table == null || entityClass == null) {
			return;
		}
		Field[] fields = entityClass.getDeclaredFields();
		if (fields == null || fields.length == 0) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("%s空字段实体", entityClass));
			}
			return;
		}
		List<Column> columns = table.getColumns();
		if (columns == null) {
			columns = new ArrayList<Column>();
			table.setColumns(columns);
		}
		
		for (Field field : fields) {
			org.walkerljl.db.api.annotation.Column columnAnnotation = 
					field.getAnnotation(org.walkerljl.db.api.annotation.Column.class);
			if (columnAnnotation == null) {
				continue;
			}
			String columnName = "";
			if ("".equals(columnAnnotation.value())) {
				columnName = field.getName();
			} else {
				columnName = columnAnnotation.value();
			}
			Column column = new Column();
			columns.add(column);
			column.setPrimaryKey(columnAnnotation.key());
			column.setName(KEY_WRAP + columnName + KEY_WRAP);
			column.setFieldName(field.getName());
			column.setJavaType(field.getType());
			
			if (column.isPrimaryKey()) {
				table.setPrimaryKey(column);
			}
		}
	}
	
	/**
	 * 获取指定类的所有父类
	 * @param clazz
	 * @return
	 */
	private static List<Class<?>> getAllSupperClasses(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		Class<?> currentSupperClass = clazz.getSuperclass();
		if (currentSupperClass == null || currentSupperClass == Object.class) {
			return null;
		}
		List<Class<?>> resultSupperClasses = new ArrayList<Class<?>>();
		resultSupperClasses.add(currentSupperClass);
		
		//递归
		List<Class<?>> supperClasses = getAllSupperClasses(currentSupperClass);
		if (CollectionUtils.isEmpty(supperClasses)) {
			return resultSupperClasses;
		}
		for (Class<?> supperClass : supperClasses) {
			resultSupperClasses.add(supperClass);
		}
		return resultSupperClasses;
	}
}