package org.walkerljl.db.orm.sql.builder;

import java.util.List;

import org.walkerljl.commons.collection.CollectionUtils;
import org.walkerljl.commons.util.ReflectUtils;
import org.walkerljl.db.orm.entity.Column;
import org.walkerljl.db.orm.entity.SqlEntry;
import org.walkerljl.db.orm.entity.Table;
import org.walkerljl.log.Logger;
import org.walkerljl.log.LoggerFactory;

/**
 *
 * InsertSqlBuilder
 *
 * @author lijunlin
 */
public class InsertSqlBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(InsertSqlBuilder.class);
	
	public static <T> SqlEntry buildAllColumnSql(Table table, T entity) throws Exception {
		List<Column> columns = table.getColumns();
        if (CollectionUtils.isNotEmpty(columns)) {
            return null;
        }
		Column firstColumn = columns.get(0);
        StringBuilder columnsText = new StringBuilder(" (").append(firstColumn.getName());
        StringBuilder valuesText = new StringBuilder(" VALUES(?");
        Object[] params = new Object[columns.size()];
        params[0] = ReflectUtils.invoke(entity, firstColumn.getGetterName());
        for (int i = 1; i < columns.size(); i++) {
        	Column column = columns.get(i);
            columnsText.append(", ").append(column.getName());
            valuesText.append(", ?");
            params[i] = ReflectUtils.invoke(entity, column.getGetterName());
        }
        columnsText.append(")");
        valuesText.append(")");
		StringBuilder sql = new StringBuilder("INSERT INTO ").append(table.getName()).append(columnsText).append(valuesText);
		if (LOG.isDebugEnabled()) {
			LOG.debug("SQL : " + sql.toString());
		}
		return new SqlEntry(sql.toString(), params);
	}
}