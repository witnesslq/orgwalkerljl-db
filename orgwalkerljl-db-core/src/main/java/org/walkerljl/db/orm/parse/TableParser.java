package org.walkerljl.db.orm.parse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.walkerljl.db.orm.annotation.Entity;
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
	
	/**
	 * 解析
	 * @param entityClass
	 * @return
	 */
	public static Table parse(Class<?> entityClass) {
		Table table = parseTable(entityClass);
		if (table == null) {
			return null;
		}
		parseColumn(table, entityClass);
		return table;
	}
	
	/**
	 * 解析表名
	 * @param entityClass
	 * @return
	 */
	public static String parseTableName(Class<?> entityClass) {
		Table table = parseTable(entityClass);
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
		table.setName(tableName);
		table.setComment(entity.comment());
		return table;
	}
	
	/**
	 * 解析字段
	 * @param table
	 * @param entityClass
	 */
	private static void parseColumn(Table table, Class<?> entityClass) {
		Field[] fields = entityClass.getDeclaredFields();
		if (fields == null || fields.length == 0) {
			LOGGER.warn(String.format("%s空字段实体", entityClass));
			return;
		}
		List<Column> columns = new ArrayList<Column>();
		table.setColumns(columns);
		for (Field field : fields) {
			org.walkerljl.db.orm.annotation.Column columnAnnotation = 
					field.getAnnotation(org.walkerljl.db.orm.annotation.Column.class);
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
			column.setName(columnName);
			column.setFieldName(field.getName());
			column.setJavaType(field.getType());
			
			if (column.isPrimaryKey()) {
				table.setPrimaryKey(column);
			}
		}
	}
	
}