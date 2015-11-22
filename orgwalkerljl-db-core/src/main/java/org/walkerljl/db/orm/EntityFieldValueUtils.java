package org.walkerljl.db.orm;

import java.util.List;

import org.walkerljl.commons.collection.CollectionUtils;
import org.walkerljl.commons.util.ReflectUtils;
import org.walkerljl.commons.util.StringUtils;
import org.walkerljl.db.orm.entity.Column;
import org.walkerljl.db.orm.entity.Table;
import org.walkerljl.db.orm.parse.TableManager;

/**
 *
 * EntityFieldValueUtils
 *
 * @author lijunlin
 */
public class EntityFieldValueUtils {

	/**
	 * 获取字段值
	 * @param entity
	 * @return
	 */
	public static Object[] getFieldValues(Object entity) {
		Table table = TableManager.getInstance().getTable(entity.getClass());
		if (table == null) {
			return null;
		}
		List<Column> columns = table.getColumns();
		if (CollectionUtils.isEmpty(columns)) {
			return null;
		}
		Object[] fieldValues = new Object[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			Column column = columns.get(i);
			String fieldName = column.getFieldName();
			if (StringUtils.isEmpty(fieldName)) {
				fieldValues[i] = null;
			} else {
				fieldValues[i] = getFieldValue(entity, column);
			}
		}
		return fieldValues;
	}
	
	/**
	 * 获取指定字段值
	 * @param entity
	 * @param column
	 * @return
	 */
	public static Object getFieldValue(Object entity, Column column) {
		if (column == null) {
			return null;
		}
		try {
			return ReflectUtils.invoke(entity, column.getGetterName());
		} catch (Exception e) {
			return null;
		}
	}
}